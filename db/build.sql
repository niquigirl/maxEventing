CREATE TABLE `RemoteSubscriber` (
`id` INT(11) NOT NULL AUTO_INCREMENT,
`restUrl` VARCHAR(255) NULL DEFAULT '0',
`timeout` INT(11) NULL DEFAULT '0',
`name` VARCHAR(255) NOT NULL DEFAULT '0',
`autoRegister` BIT(1) NULL DEFAULT b'0',
`topic` VARCHAR(50) NOT NULL DEFAULT '0',
`filterString` VARCHAR(512) NULL DEFAULT '0',
PRIMARY KEY (`id`),
UNIQUE INDEX `name_topic` (`name`, `topic`)
)
ENGINE=InnoDB;

insert into remotesubscriber(resturl, timeout, name, autoregister, topic, filterstring) values
  ('http://localhost:8080/maxeventing/testDIHandler',30000,'DefaultDITester',1,'DataIntegrity','verb=''TestDIRest'''),
  ('http://localhost:8080/maxeventing/testUAHandler',30000,'DefaultUATester',1,'Activity','verb=''TestUARest'''),
  ('http://localhost:8080/maxsvcs/1.0/en/us/onAutoOrderLocaleDIViolation',30000,'maxsvcs.AutoOrderLocaleDIViolationHandler',1,'DataIntegrity','verb=''AutoOrderCountryWarehouseCurrencyDIViolationFound''')

