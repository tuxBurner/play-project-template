package neo4j;

import neo4j.repositories.users.NeoTokenRepository;
import neo4j.repositories.users.NeoUserRepository;
import neo4jplugin.Neo4JPlugin;
import neo4jplugin.ServiceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Neo4JServiceProvider extends ServiceProvider {

    @Autowired
    public NeoUserRepository neoUserRepository;

    @Autowired
    public NeoTokenRepository neoTokenRepository;

    public static Neo4JServiceProvider get() {
        return Neo4JPlugin.get();
    }

}
