## XPath Queries

- Search for Root Element: ```/``` <- return complete document
- Search for Root Element: ```collection``` <- return content of tag "collection"
- Search for Root Element: ```collection/cd``` <- returns the element(s) inside collection of "cd" -> here in it is an
  array
- Search for Element with special attribute -> here year = 1994
  ```collection//cd[@year=1994]```
- 