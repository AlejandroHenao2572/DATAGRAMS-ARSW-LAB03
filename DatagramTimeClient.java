import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatagramTimeClient {

    private static final String SERVER_IP = "127.0.0.1"; //ip del servidor
    private static final int SERVER_PORT = 4445; //Puerto del servidor
    private static final int UPDATE_INTERVAL = 5000; // 5 segundos en milisegundos
    private static final int SOCKET_TIMEOUT = 3000;  // 3 segundos de espera para de respuesta
    private static String LastKnowTime = "Sin hora disponible";


    public static void main(String[] args) {

        try {
            DatagramSocket socket = new DatagramSocket(); //Crea un socket reutilizable

            socket.setSoTimeout(SOCKET_TIMEOUT); //Configurar tiempo de espera para la respuesta

            InetAddress address = InetAddress.getByName(SERVER_IP); //Obtiene la direccion IP del servidor 

            //Ciclo Infinito cada 5 segundos
            while (true) {
                try {
                    byte[] buf = new byte[256]; //Crea un buffer para enviar y recibir datos

                    DatagramPacket packet =
                            new DatagramPacket(buf, buf.length, address, SERVER_PORT); // Crea un parquete UDP dirigido servidor

                    socket.send(packet); //Envia el paquete al servidor(datagrama vacio)

                    packet = new DatagramPacket(buf, buf.length); //Prepara un nuevo paquete para recibir respuesta
                    socket.receive(packet); //Envia el paquete al servidor

                    //Actulizar la hora si se recibio
                    LastKnowTime  = new String(packet.getData(), 0, packet.getLength()); //Convierte los datos recibidos en un string
                    
                    System.out.println("Date: " + LastKnowTime); //Imprime la fecha recibida por el servidor

                } catch (SocketTimeoutException e) {
                    System.out.println("Servidor no disponible. Ultima hora conocida:" + LastKnowTime);
                } 
                Thread.sleep(UPDATE_INTERVAL); //Esperar 5 segundos antes de la siguiente actualizacion  
            }

        } catch (Exception ex) {
            Logger.getLogger(DatagramTimeClient.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
