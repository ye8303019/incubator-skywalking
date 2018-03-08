/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.skywalking.apm.collector.agent.grpc.provider.handler;

import com.google.protobuf.ProtocolStringList;
import io.grpc.stub.StreamObserver;
import org.apache.skywalking.apm.collector.analysis.register.define.AnalysisRegisterModule;
import org.apache.skywalking.apm.collector.analysis.register.define.service.INetworkAddressIDService;
import org.apache.skywalking.apm.collector.core.module.ModuleManager;
import org.apache.skywalking.apm.collector.server.grpc.GRPCHandler;
import org.apache.skywalking.apm.network.proto.KeyWithIntegerValue;
import org.apache.skywalking.apm.network.proto.NetworkAddressMappings;
import org.apache.skywalking.apm.network.proto.NetworkAddressRegisterServiceGrpc;
import org.apache.skywalking.apm.network.proto.NetworkAddresses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author peng-yongsheng
 */
public class NetworkAddressRegisterServiceHandler extends NetworkAddressRegisterServiceGrpc.NetworkAddressRegisterServiceImplBase implements GRPCHandler {

    private final Logger logger = LoggerFactory.getLogger(NetworkAddressRegisterServiceHandler.class);

    private final INetworkAddressIDService networkAddressIDService;

    public NetworkAddressRegisterServiceHandler(ModuleManager moduleManager) {
        this.networkAddressIDService = moduleManager.find(AnalysisRegisterModule.NAME).getService(INetworkAddressIDService.class);
    }

    @Override
    public void batchRegister(NetworkAddresses request, StreamObserver<NetworkAddressMappings> responseObserver) {
        logger.debug("register application");
        ProtocolStringList addressesList = request.getAddressesList();

        NetworkAddressMappings.Builder builder = NetworkAddressMappings.newBuilder();
        for (String networkAddress : addressesList) {
            int addressId = networkAddressIDService.get(networkAddress);

            if (addressId != 0) {
                KeyWithIntegerValue value = KeyWithIntegerValue.newBuilder().setKey(networkAddress).setValue(addressId).build();
                builder.addAddressIds(value);
            }
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
