## Exercise 7

### Task 1:

- Import Stations:

```bash
LOAD CSV FROM 'file:///vbb_neo4j.csv' as row
WITH row WHERE row[0] <> 'ID'
CREATE (:Station {id: row[0], stationName: row[1], district:row[2], lat: toFloat(row[3]), lng: toFloat(row[4])})
```

- Import Edges:

```bash
LOAD CSV FROM 'file:///vbb_neo4j_edge.csv' as row
WITH row WHERE row[0] <> 'ID'
MATCH (s1:Station{id:row[0]}) 
MATCH (s2:Station{id:row[1]}) 
CREATE (s1)-[:ConnectedTo {typeId: row[2], cost: row[3]}]->(s2); // alternative: toInteger bei beiden nutzen
```

### Task 2:

Queries:

- Display all districts:
    ```bash
    MATCH (a: Station) RETURN DISTINCT a.district
  ```
  
- Determine the number of stops per districts:
    ```bash
    MATCH (a: Station) RETURN DISTINCT a.district, count(a) as numberOfStops
    ```
- Determine the number of stops per district that can be reached by metro to the next station:
   ```bash 
    MATCH (a: Station)-[:ConnectedTo]->(b:Station) WHERE a.district = b.district RETURN DISTINCT a.district, count(b) as numberOfStops
  ```

- Show all direct connections to Pankow 
  ```bash
    MATCH (a: Station)-[:ConnectedTo]->(b:Station) WHERE b.stationName = "S+U Pankow (Berlin)" RETURN  a, b
    ```
- List incoming and outgoing connections per stop
  ```bash
  MATCH (stop:Station)-[incoming:ConnectedTo]->(incomingStop:Station)
  OPTIONAL MATCH (stop)-[outgoing:ConnectedTo]->(outgoingStop:Station)
  RETURN stop.stationName AS stopName, COLLECT(incomingStop.stationName) AS incomingConnections, COLLECT(outgoingStop.stationName) AS outgoingConnections
  ORDER BY stopName;
  ```

- Show all destination stops reachable within 400 seconds
  ```bash 
  MATCH path = (start: Station)-[r:ConnectedTo]->(destination:Station)
  where toInteger(r.cost) <= 400
  RETURN start.stationName, destination.stationName, toInteger(r.cost); 
  ```

- Check for direct connections from S+U Berlin Hauptbahnhof to U Turmstr. (Ber- lin) - how does the query look?
  ```bash
  MATCH path = shortestPath((start:Station {stationName: 'S+U Berlin Hauptbahnhof'})-[:ConnectedTo*..3]->(destination:Station {stationName: 'U Turmstr. (Berlin)'}))
  RETURN nodes(path) AS stops, relationships(path) AS connections, length(path) AS numberOfStops
  ORDER BY numberOfStops;
  ```
- Find paths with a maximum of 4 stops from S+U Berlin Hauptbahnhof to U Viktoria-Luise-Platz (Berlin) and calculate travel times. Sort the output by travel time (ascending).
  ```bash
  MATCH path = shortestPath((start:Station {stationName: 'S+U Berlin Hauptbahnhof'})-[:ConnectedTo*..4]->(destination:Station {stationName: 'U Viktoria-Luise-Platz (Berlin)'}))
  RETURN nodes(path) AS stops, relationships(path) AS connections, length(path) AS numberOfStops, REDUCE(s = 0, rel IN relationships(path) | s + toInteger(rel.cost)) AS totalTime
  ORDER BY totalTime ASC;
  ```