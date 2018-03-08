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

package org.apache.skywalking.apm.collector.storage.h2.dao.cache;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.skywalking.apm.collector.client.h2.H2Client;
import org.apache.skywalking.apm.collector.client.h2.H2ClientException;
import org.apache.skywalking.apm.collector.storage.base.sql.SqlBuilder;
import org.apache.skywalking.apm.collector.storage.dao.cache.IApplicationCacheDAO;
import org.apache.skywalking.apm.collector.storage.h2.base.dao.H2DAO;
import org.apache.skywalking.apm.collector.storage.table.register.Application;
import org.apache.skywalking.apm.collector.storage.table.register.ApplicationTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author peng-yongsheng, clevertension
 */
public class ApplicationH2CacheDAO extends H2DAO implements IApplicationCacheDAO {

    private final Logger logger = LoggerFactory.getLogger(ApplicationH2CacheDAO.class);

    private static final String GET_APPLICATION_ID_SQL = "select {0} from {1} where {2} = ? and {3} = ?";
    private static final String GET_APPLICATION_SQL = "select {0},{1} from {2} where {3} = ?";

    public ApplicationH2CacheDAO(H2Client client) {
        super(client);
    }

    @Override
    public int getApplicationIdByCode(String applicationCode) {
        logger.info("get the application id with application code = {}", applicationCode);
        H2Client client = getClient();
        String sql = SqlBuilder.buildSql(GET_APPLICATION_ID_SQL, ApplicationTable.COLUMN_APPLICATION_ID, ApplicationTable.TABLE, ApplicationTable.COLUMN_APPLICATION_CODE, ApplicationTable.COLUMN_IS_ADDRESS);

        Object[] params = new Object[] {applicationCode, false};
        try (ResultSet rs = client.executeQuery(sql, params)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException | H2ClientException e) {
            logger.error(e.getMessage(), e);
        }
        return 0;
    }

    @Override public Application getApplication(int applicationId) {
        logger.debug("get application code, applicationId: {}", applicationId);
        H2Client client = getClient();
        String sql = SqlBuilder.buildSql(GET_APPLICATION_SQL, ApplicationTable.COLUMN_APPLICATION_CODE, ApplicationTable.COLUMN_IS_ADDRESS, ApplicationTable.TABLE, ApplicationTable.COLUMN_APPLICATION_ID);
        Object[] params = new Object[] {applicationId};
        try (ResultSet rs = client.executeQuery(sql, params)) {
            if (rs.next()) {
                Application application = new Application();
                application.setApplicationId(applicationId);
                application.setApplicationCode(rs.getString(1));
                application.setIsAddress(rs.getInt(2));
                return application;
            }
        } catch (SQLException | H2ClientException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override public int getApplicationIdByAddressId(int addressId) {
        logger.info("get the application id with address id = {}", addressId);
        H2Client client = getClient();
        String sql = SqlBuilder.buildSql(GET_APPLICATION_ID_SQL, ApplicationTable.COLUMN_APPLICATION_ID, ApplicationTable.TABLE, ApplicationTable.COLUMN_ADDRESS_ID, ApplicationTable.COLUMN_IS_ADDRESS);

        Object[] params = new Object[] {addressId, true};
        try (ResultSet rs = client.executeQuery(sql, params)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException | H2ClientException e) {
            logger.error(e.getMessage(), e);
        }
        return 0;
    }
}
