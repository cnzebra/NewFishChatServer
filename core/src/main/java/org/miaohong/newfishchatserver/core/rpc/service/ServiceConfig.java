package org.miaohong.newfishchatserver.core.rpc.service;

import lombok.Getter;
import org.miaohong.newfishchatserver.core.rpc.eventbus.EventBus;
import org.miaohong.newfishchatserver.core.rpc.eventbus.EventBusManager;
import org.miaohong.newfishchatserver.core.rpc.registry.zk.ZookeeperRegistry;
import org.miaohong.newfishchatserver.core.rpc.server.ServerConfig;
import org.miaohong.newfishchatserver.core.rpc.server.transport.handler.NettyServerMessageHandler;


public class ServiceConfig<T> {

    @Getter
    private final EventBus eventBus = EventBusManager.get();
    private ServerConfig serverConfig;
    private T ref;
    private String interfaceId;
    private ServiceBootstrap serviceBootstrap;

    public ServiceConfig() {
        eventBus.register(new NettyServerMessageHandler.RpcServerHandlerListener());
    }

    public synchronized void export() {
        if (serviceBootstrap == null) {
            serviceBootstrap = new ServiceBootstrap<>(new ZookeeperRegistry(), this);
        }
        serviceBootstrap.export();
    }

    public synchronized void unExport() {
        if (serviceBootstrap != null) {
            serviceBootstrap.unExport();
        }
    }

    public T getRef() {
        return ref;
    }

    public ServiceConfig setRef(T ref) {
        this.ref = ref;
        return this;
    }

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public ServiceConfig setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
        return this;
    }

    public String getInterfaceId() {
        return interfaceId;
    }

    public ServiceConfig setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
        return this;
    }


}
