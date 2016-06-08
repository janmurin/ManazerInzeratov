package home.managerinzeratov;

import org.hsqldb.Server;

public class SpustacDatabazy implements Runnable{
    
    public void  execute(){
        Server server =new Server();
        server.setDatabaseName(0,"inzeratydb");
        server.setDatabasePath(0 ,"db/inzeratydb");
        server.setPort(1235);
        server.start();
          
    }
    public static void main(String[] args) {
        Server server =new Server();
        server.setDatabaseName(0,"inzeratydb");
        server.setDatabasePath(0 ,"db/inzeratydb");
        server.setPort(1235);
        server.start();
    }

    public void run() {
       execute();
    }
}
