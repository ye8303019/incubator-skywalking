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

package org.apache.skywalking.apm.collector.analysis.alarm.provider.worker.application;

import org.apache.skywalking.apm.collector.storage.table.alarm.ApplicationAlarmList;

/**
 * @author peng-yongsheng
 */
public class ApplicationMetricAlarmListCopy {

    public static ApplicationAlarmList copy(ApplicationAlarmList applicationAlarmList) {
        ApplicationAlarmList newApplicationAlarmList = new ApplicationAlarmList();
        newApplicationAlarmList.setMetricId(applicationAlarmList.getMetricId());

        newApplicationAlarmList.setAlarmContent(applicationAlarmList.getAlarmContent());
        newApplicationAlarmList.setAlarmType(applicationAlarmList.getAlarmType());
        newApplicationAlarmList.setSourceValue(applicationAlarmList.getSourceValue());
        newApplicationAlarmList.setApplicationId(applicationAlarmList.getApplicationId());
        newApplicationAlarmList.setTimeBucket(newApplicationAlarmList.getTimeBucket());
        return newApplicationAlarmList;
    }
}
