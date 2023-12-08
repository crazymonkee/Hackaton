package repository;

import connection.Connection;
import models.Model;
import models.User;

import java.util.ArrayList;
import java.util.Arrays;

public class UserRepository implements Repository {

    @Override
    public ArrayList<Model> find(String column, String[] conditions, boolean join, String joinTable, Connection connection) {
        ArrayList<String[]> userData = connection.readData();
        ArrayList<Model> result = new ArrayList<>();

        for (String[] row : userData) {
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
                User user = new User();
                user.setId(Integer.parseInt(row[0])); // Assuming id is the first column
                user.setName(row[1]); // Assuming name is the second column
                user.setNim(row[2]); // Assuming nim is the third column
                user.setTeamName(row[3]); // Assuming teamName is the fourth column

                result.add(user);
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
        ArrayList<String[]> userData = connection.readData();

        // Auto-increment the ID
        int newId = userData.size() + 1;
        data[0] = String.valueOf(newId);

        userData.add(data);
        connection.writeData(userData);

        User newUser = new User();
        newUser.setId(newId);
        newUser.setName(data[1]); // Assuming name is the second column
        newUser.setNim(data[2]); // Assuming nim is the third column
        newUser.setTeamName(data[3]); // Assuming teamName is the fourth column

        return newUser;
    }
}

