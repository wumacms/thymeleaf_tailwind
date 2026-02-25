# Thymeleaf 模板问题与解决方案总结

本文档总结站点开发过程中遇到的模板与数据访问问题及其解决方案，便于后续维护和排查类似错误。

---

## 1. 数据库连接被拒绝

**现象**：访问首页时报错  
`Access denied for user 'root'@'192.168.65.1' (using password: YES)`

**原因**：MySQL 只允许 `root@localhost` 连接。应用通过 Docker/虚拟机或本机另一网卡连接时，MySQL 看到的客户端地址是 `192.168.65.1`，未对该主机授权。

**解决方案**：
- 在 MySQL 中执行授权（将 `admin123` 改为实际密码）：
  ```sql
  CREATE USER IF NOT EXISTS 'root'@'%' IDENTIFIED BY 'admin123';
  GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;
  FLUSH PRIVILEGES;
  ```
- 本地密码可放在 `application-local.properties` 中覆盖，避免提交到 Git（该文件已在 `.gitignore` 中）。
- 更多说明见：`docs/数据库连接说明.md`。

---

## 2. 模板解析报错：Property or field 'content' cannot be found on null

**现象**：渲染 `index.html` 时出错，提示在求值 `block.content` 时对象为 null。

**原因**：  
- `blocks` 列表中可能含有 null 元素，或某条区块的 `block_type`/`content` 为空。  
- 同一标签上同时写 `th:if` 和 `th:replace` 时，Thymeleaf 仍会先求值 `th:replace` 中的表达式，导致在 block 为 null 时访问 `block.content` 报错。

**解决方案**：
- **模板**：用两层 `th:block` 分离“循环”和“有条件替换”，只在 block 非空且 `blockType` 非空时求值并替换：
  ```html
  <th:block th:each="block : ${blocks}">
      <th:block th:if="${block != null and block.blockType != null}">
          <div th:replace="~{...}"></div>
      </th:block>
  </th:block>
  ```
- **后端**：在 `BlockServiceImpl.getPageBlocks()` 中过滤掉 null 或无效 `blockType` 的区块，并对每条区块保证 `content` 不为 null（无内容或解析失败时设为 `Collections.emptyMap()`）。

---

## 3. 动态模板名未被求值

**现象**：报错找不到模板  
`Error resolving template [fragments/sections/${block.blockType}]`，路径中的 `${block.blockType}` 被当作字面量。

**原因**：在 Thymeleaf 的片段表达式 `~{ templateName :: fragmentName }` 中，模板名若写成 `fragments/sections/${block.blockType}`，其中的 `${...}` 不会在模板名部分被求值。

**解决方案**：用字符串拼接得到模板名，让表达式先求值再参与解析：
```html
th:replace="~{${'fragments/sections/' + block.blockType} :: ${block.blockType}(blockContent=${block.content})}"
```

---

## 4. Map 类型数据用点号访问报错（LinkedHashMap）

**现象**：  
- `Property or field 'bio' cannot be found on object of type 'java.util.LinkedHashMap'`（team 模板）  
- `Property or field 'isPopular' cannot be found on object of type 'java.util.LinkedHashMap'`（pricing 模板）

**原因**：区块内容来自 JSON，解析后为 `Map`（如 `LinkedHashMap`）。在 SpEL 中用 `member.bio`、`plan.isPopular` 等会按 JavaBean 属性解析，而 `Map` 没有这些属性，因此报错。

**解决方案**：对来自 JSON 的 Map 使用方括号访问键，例如：
- `member.bio` → `member['bio']`
- `plan.isPopular`、`plan.price`、`plan.name` 等 → `plan['isPopular']`、`plan['price']`、`plan['name']` 等

这样会走 `Map.get(key)`，键不存在时返回 null，不会抛错。

---

## 5. Integer 被当作 Boolean 使用导致转换错误

**现象**：  
`cannot convert from java.lang.Integer to java.lang.Boolean`，出现在求值 `!plan['price']` 时（pricing 模板约第 29 行）。

**原因**：JSON 中 `price` 为数字（如 49、99），`plan['price']` 类型为 Integer。`th:if="${!plan['price']}"` 会对该值做逻辑非，SpEL 需要先转为 Boolean，而 Spring 默认不支持 Integer → Boolean 的转换。

**解决方案**：不用“取反”表示“无价格”，改为显式判断 null：
- 有价格时显示金额：`th:if="${plan['price'] != null}"`
- 无价格时显示“定制”：`th:if="${plan['price'] == null}"`

---

## 小结

| 问题类型           | 关键点                                       | 处理方式 |
|--------------------|----------------------------------------------|----------|
| 数据库连接         | 主机/用户授权                                | MySQL 授权或本地配置覆盖 |
| null 安全          | 列表元素或 content 可能为 null              | 模板用 th:block 分层 + 后端过滤与默认空 Map |
| 动态模板名         | `~{...}` 中模板名不参与变量求值             | 用 `${'...' + var}` 拼接模板名 |
| JSON Map 访问      | 点号按 JavaBean 解析，Map 无对应属性         | 统一用 `map['key']` 访问 |
| 类型与布尔上下文   | Integer 等不能直接当 Boolean 取反            | 用 `== null` / `!= null` 等布尔表达式 |

遵循以上方式后，站点可正常从数据库加载站点、页面与区块并渲染 Thymeleaf 模板。
