var ioc = {
  dao: {
    type: "org.nutz.dao.impl.NutDao",
    args: [{refer: "dataSource"}]
  },
  dataSource: {
    type: "org.apache.commons.dbcp.BasicDataSource",
    fields: {
      driverClassName: "com.mysql.jdbc.Driver",
      url: "jdbc:mysql://127.0.0.1:3306/weixin",
      username: "root",
      password: "123456",
      validationQuery: "select 1"
    },
    events: {
      depose: "close"
    }
  }
};