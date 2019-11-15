package org.miaohong.newfishchatserver.client.client;

import org.miaohong.newfishchatserver.core.rpc.client.ConnectManager;
import org.miaohong.newfishchatserver.core.rpc.client.RpcClient;
import org.miaohong.newfishchatserver.proto.gateway.GatewayProto;
import org.miaohong.newfishchatserver.proto.gateway.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public class Client {

    private static final Logger LOG = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        RpcClient rpcClient = new RpcClient("127.0.0.1:15000");

        ConnectManager.getInstance().updateConnectedServer(Collections.singletonList("127.0.0.1:15000"));
        GatewayProto s = rpcClient.getProxy(GatewayProto.class);
        Person person = s.person();
        System.out.println(person);
        LOG.info("result is {}", person);
    }
}