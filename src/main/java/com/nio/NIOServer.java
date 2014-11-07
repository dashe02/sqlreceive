package com.nio;

import com.conf.ConfigParse;
import com.util.Constant;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: hadoop
 * Date: 14-11-6
 * Time: 下午10:46
 * To change this template use File | Settings | File Templates.
 */
public class NIOServer {
    /*标识数字*/
    private int flag=0;
    /*缓冲池大小*/
    private int BLOCK=4096;
    /*接受数据缓冲池*/
    private ByteBuffer sendBuffer=ByteBuffer.allocate(BLOCK);
    /*发送数据缓冲池*/
    private ByteBuffer receiveBuffer=ByteBuffer.allocate(BLOCK);

    private Selector selector;

    private NIOServer(int port) throws IOException {
        //打开服务器套接字通道
        ServerSocketChannel serverSocketChannel=ServerSocketChannel.open();
        //服务器配置为非阻塞
        serverSocketChannel.configureBlocking(false);
        //检索与此通道关联的服务器套接字
        ServerSocket serverSocket=serverSocketChannel.socket();
        //进行服务的绑定
        serverSocket.bind(new InetSocketAddress(port));
        //通过open()方法找到Selector
        selector=Selector.open();
        //注册到selector，等待连接
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("Server Start-----" + port);
    }
    //监听
    private void listen() throws IOException{
        while (true){
            //选择一组键,并且相应的通道已经打开
            selector.select();
            //返回此选择器的已选择键集
            Set<SelectionKey> selectionKeys=selector.selectedKeys();
            Iterator<SelectionKey> iterator=selectionKeys.iterator();
            while(iterator.hasNext()){
                SelectionKey selectionKey=iterator.next();
                iterator.remove();
                handleKey(selectionKey);
            }
        }
    }
    //处理请求
    private void handleKey(SelectionKey selectionKey) throws IOException{
        //接受请求
        ServerSocketChannel server=null;
        SocketChannel client=null;
        String receiveText;
        String sendText;
        int count=0;
        //测试此键的通道是否已准备好接受新的套接字连接
        if(selectionKey.isAcceptable()){
            //返回为之创建此键的通道
            server=(ServerSocketChannel)selectionKey.channel();
            //接受到此通道套接字的连接
            //此方法返回的套接字通道(如果有）将处于阻塞模式
            client=server.accept();
            //配置为阻塞
           client.configureBlocking(false);
            //注册到selector,等待连接
            client.register(selector,SelectionKey.OP_READ);
        }else if(selectionKey.isReadable()){
            //返回为之创建此键的通道
            client=(SocketChannel)selectionKey.channel();
            //将缓冲区清空以备下次读取
            receiveBuffer.clear();
            //读取服务器发送来的数据到缓冲区中
            count=client.read(receiveBuffer);
            if(count>0){
                receiveText=new String(receiveBuffer.array(),0,count);
                System.out.println("服务器端接受客户端数据--:" + receiveText);
                client.register(selector,SelectionKey.OP_WRITE);
            }
        }else if(selectionKey.isWritable()){
            //将缓冲区清空以备下次写入
            sendBuffer.clear();
            //返回之前创建此键的通道
            client=(SocketChannel)selectionKey.channel();
            sendText="message from server--"+flag++;
            //向缓冲区中输入数据
            sendBuffer.put(sendText.getBytes());
            //将缓冲区各标志复位,因为向里面put了数据标志被改变要想从中读取数据发向服务器,就要复位
            sendBuffer.flip();
            //输出到通道
            client.write(sendBuffer);
            System.out.println("服务器端向客户端发送数据--:" + sendText);
            client.register(selector,SelectionKey.OP_READ);
        }
    }
    public static void main(String[] args) throws IOException{
        ConfigParse configParse=new ConfigParse();
        int port=Integer.parseInt(configParse.parseConfig().getProperty(Constant.port));
        NIOServer server=new NIOServer(port);
        server.listen();
    }
}
