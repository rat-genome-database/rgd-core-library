RGDCORE 1.1.3
-------------

General notes:

All new java applications should access databases through DAO classes, and as the base class
they should use AbstractDAO class from this package.

For security and ease of management reasons, the application should NEVER use hardcoded connection strings;
the application configuration file, like AppConfigure.xml, should not contain connection strings;
the connection will be acquired by AbstractDAO according to the current policy.

