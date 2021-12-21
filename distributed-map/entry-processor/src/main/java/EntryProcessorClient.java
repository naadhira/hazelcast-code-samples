import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.EntryProcessor;
import com.hazelcast.map.IMap;

import java.util.Map;

public class EntryProcessorClient {

    public static void main(String[] args) {
        ClientConfig config = new ClientConfig();
        config.getUserCodeDeploymentConfig()
                .setEnabled(true)
                .addClass(Employee.class)
                .addClass(SalaryIncreaseEntryProcessor.class);

        HazelcastInstance hz = HazelcastClient.newHazelcastClient(config);
        IMap<String, Employee> employees = hz.getMap("employees");
        employees.put("John", new Employee(1000));
        employees.put("Mark", new Employee(1000));
        employees.put("Spencer", new Employee(1000));

        employees.executeOnEntries(new SalaryIncreaseEntryProcessor());

        for (Map.Entry<String, Employee> entry : employees.entrySet()) {
            System.out.println(entry.getKey() + " salary: " + entry.getValue().getSalary());
        }

        Hazelcast.shutdownAll();
    }
}
