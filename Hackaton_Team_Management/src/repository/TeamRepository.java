package repository;

import connection.Connection;
import models.Model;
import models.Team;

import java.util.ArrayList;
import java.util.Arrays;

public class TeamRepository implements Repository {

    @Override
    public ArrayList<Model> find(String column, String[] conditions, boolean join, String joinTable, Connection connection) {
        ArrayList<String[]> teamData = connection.readData();
        ArrayList<Model> result = new ArrayList<>();

        for (String[] row : teamData) {
            boolean meetsConditions = true;

            // Check conditions
            if (conditions != null) {
                for (String condition : conditions) {
                    String[] parts = condition.split(";");
                    String conditionColumn = parts[0];
                    String operator = parts[1];
                    String value = parts[2];

                    int columnIndex = Arrays.asList(row).indexOf(conditionColumn);

                    if (columnIndex != -1) {
                        String rowData = row[columnIndex];

                        // Check the condition
                        switch (operator) {
                            case "=":
                                meetsConditions &= rowData.equals(value);
                                break;
                            // Add other operators as needed
                        }
                    }
                }
            }

            if (meetsConditions) {
                Team team = new Team();
                team.setId(Integer.parseInt(row[0])); // Assuming id is the first column
                team.setName(row[1]); // Assuming name is the second column

                result.add(team);
            }
        }

        return result.isEmpty() ? null : result;
    }

    @Override
    public Model findOne(String column, String[] conditions, boolean join, String joinTable, Connection connection) {
        ArrayList<Model> result = find(column, conditions, join, joinTable, connection);
        return result != null && !result.isEmpty() ? result.get(0) : null;
    }

    @Override
    public Model insert(String[] data, Connection connection) {
        ArrayList<String[]> teamData = connection.readData();

        // Auto-increment the ID
        int newId = teamData.size() + 1;
        data[0] = String.valueOf(newId);

        teamData.add(data);
        connection.writeData(teamData);

        Team newTeam = new Team();
        newTeam.setId(newId);
        newTeam.setName(data[1]); // Assuming name is the second column

        return newTeam;
    }
}
