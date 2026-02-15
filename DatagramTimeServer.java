import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatagramTimeServer {

    DatagramSocket socket;

    public DatagramTimeServer() {
        try {
            //Crea un sockey en el puerto 4445
            socket = new DatagramSocket(4445);
        } catch (SocketException ex) {
            Logger.getLogger(DatagramTimeServer.class.getName())
                  .log(Level.SEVERE, null, ex);
        }
    }

    public void startServer() {

        System.out.println("Servidor de tiempo iniciado en el puerto 4445");

        //Bucle para aternder a multiples solicitudes
        while (true){
            byte[] buf = new byte[256]; //Crea un buffer de 256 bytes para recibir datos

            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length); //Crea un packete UDP para recibir los datos
                socket.receive(packet); //El socket del servidor espera que llegue el paquete 

                String dString = new Date().toString(); //Obtiene la fecha y hora del sistema
                buf = dString.getBytes(); //Convierte la fecha en bytes

                InetAddress address = packet.getAddress(); //Obtiene la direccion del cliente que envio el paquete
                int port = packet.getPort(); // Obtiene el puerto del cliente que enviio el packete

                packet = new DatagramPacket(buf, buf.length, address, port); //Crea un nuevo paquete con la fecha, longitud, dirrecion ip y puerto
                socket.send(packet); //envia el packete con la respuesta a el cliente

            } catch (IOException ex) {
                Logger.getLogger(DatagramTimeServer.class.getName())
                    .log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void main(String[] args) {
        //Crea una instancia del servidor y lo inicia
        DatagramTimeServer ds = new DatagramTimeServer();
        ds.startServer();
    }
}
