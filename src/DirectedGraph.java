/*
@author Jiaxi Chen
*/


import java.util.*;


public class DirectedGraph<V> implements IGraph<V> {

    private Map<V, Vertex> vertices = new TreeMap<>();

    class Vertex {
        LinkedList<V> pointTo = new LinkedList<>();
        LinkedList<V> pointBy = new LinkedList<>();
    }

    @Override
    public String toString() {
        String graph = "";
        int connections = 0;
        for (V v : vertices.keySet()) {
            connections += vertices.get(v).pointTo.size();
            graph += (v + ": ");
            for(V point : vertices.get(v).pointTo)
                graph += (point + ", ");
            graph += ("\n");
        }
        String toReturn = String.format("vertices: %d \n connections: %d ", vertices.size(), connections);
        toReturn += graph;
        return toReturn;
    }

    @Override
    public void add(V vertexName) {
        vertices.put(vertexName, new Vertex());
    }

    @Override
    public boolean contains(V label) {
        return vertices.containsKey(label);
    }

    @Override
    public void connect(V start, V destination) {
        if (contains(start) && contains(destination)) {
            vertices.get(start).pointTo.add(destination);
            vertices.get(destination).pointBy.add(start);
        } else
            throw new NoSuchElementException("vertex not exists.");
    }

    @Override
    public void disconnect(V start, V destination) {
        if (contains(start) && contains(destination)) {
            vertices.get(start).pointTo.remove(destination);
            vertices.get(destination).pointBy.remove(start);
        } else
            throw new NoSuchElementException("vertex not exists.");
    }

    @Override
    public boolean isConnected(V start, V destination) {
        return isConnected(start, destination, new LinkedList<V>());
    }

    private boolean isConnected(V current, V destination, LinkedList visited) {
        if (vertices.get(current).pointTo.contains(destination)) {
            visited.add(current);
            return true;
        } else if (visited.contains(current))
            return false;

        visited.add(current);
        for (V v : vertices.get(current).pointTo) {
            if (vertices.get(v).pointTo.size() == 0)
                continue;
            else if (isConnected(v, destination, visited))
                return true;
        }
        return false;
    }

    @Override
    public void clear() {
        vertices = new TreeMap<>();
    }

    @Override
    public int size() {
        return vertices.size();
    }

    @Override
    public Iterable<V> vertices() {
        return vertices.keySet();
    }

    @Override
    public Iterable<V> neighbors(V vertexName) {
        if (contains(vertexName))
            return vertices.get(vertexName).pointTo;
        else
            throw new NoSuchElementException("vertex not exists.");
    }

    @Override
    public void remove(V vertexName) {
        if (!contains(vertexName))
            throw new NoSuchElementException("vertex not exists.");

        for (V currentVertex : vertices.get(vertexName).pointTo) {
            vertices.get(currentVertex).pointBy.remove(vertexName);
        }
        for (V currentVertex : vertices.get(vertexName).pointBy) {
            vertices.get(currentVertex).pointTo.remove(vertexName);
        }
        vertices.remove(vertexName);
    }

    @Override
    public List<V> shortestPath(V start, V destination) {
        if (!contains(start) || !contains(destination))
            throw new NoSuchElementException("vertex not exists.");

        //vertices set
        List<V> path = new LinkedList<>();
        List<V> visited = new LinkedList<>();
        Queue<V> queue = new PriorityQueue<>();

        //add starting point
        queue.add(start);
        path.add(destination);

        boolean test = true;
        V next = start;
        if (!isConnected(start, destination)) {
            path.clear();
            test = false;
        }
        while (test) {
            if (start.equals(destination)) {
                test = false;
                break;
            }
            if (!isConnected(next, destination)) {
                if (queue.isEmpty()) {
                    path.clear();
                    return path;
                } else {
                    visited.add(next);
                    next = queue.remove();
                }
                continue;
            }
            for (V v : neighbors(next)) {
                if (!visited.contains(v) && !queue.contains(v))
                    if (v.equals(destination)) {
                        path.add(0, next);
                        destination = next;
                        visited.clear();
                        queue.clear();
                        queue.add(start);
                        next = start;
                        break;
                    } else {
                        queue.add(v);
                    }
            }
            visited.add(next);
            next = queue.remove();
        }
        return path;
    }

    @Override
    public IGraph<V> connectedGraph(V origin) {
        if (!contains(origin))
            throw new NoSuchElementException("vertex not exists.");
        List<V> visited = new LinkedList<>();
        Queue<V> visit = new PriorityQueue<>();
        visit.addAll(vertices.get(origin).pointTo);
        DirectedGraph<V> graph = new DirectedGraph<>();

        graph.add(origin);
        for (V v : vertices.get(origin).pointTo) {
            graph.vertices.get(origin).pointTo.add(v);
        }

        while (!visit.isEmpty()) {
            V vertex = visit.remove();
            visited.add(vertex);
            graph.add(vertex);

            for (V v : vertices.get(vertex).pointTo) {
                graph.vertices.get(vertex).pointTo.add(v);
                if (!visited.contains(v))
                    visit.add(v);
            }
        }
        return graph;
    }
}
