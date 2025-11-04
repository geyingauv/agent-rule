- 使用 playwright 工具可以访问后端 openApi 接口
  获取所有分组`http://localhost:9999/api/swagger/groups`
  根据业务分组获取接口文档`http://localhost:9999/api/v3/api-docs/${group}`
- 每获取一个分组接口文档后创建/更新`/api`路径下的 API endpoint
