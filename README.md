As the name implies this app serves as the SQL-editor trough which user can communicate with remote datebase. Before sending query to datebase it first runs trough checker, which looks for syntax and semantic type of errors, and only after it passes checker query is sent to database. Arcitecture use for this project is MVC + using observer for GUI updates and bridge pattern to communicate with database.

Other functionalities:
- bulk import: importing data from csv into database
- export: exporting results of given query into csv


<img align="center" src="https://user-images.githubusercontent.com/92781927/170845837-1173de98-e7fa-4d2d-9af5-ec4ee9e3c827.png">
