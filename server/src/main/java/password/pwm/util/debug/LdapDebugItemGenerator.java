/*
 * Password Management Servlets (PWM)
 * http://www.pwm-project.org
 *
 * Copyright (c) 2006-2009 Novell, Inc.
 * Copyright (c) 2009-2021 The PWM Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package password.pwm.util.debug;

import password.pwm.PwmConstants;
import password.pwm.ldap.LdapDebugDataGenerator;
import password.pwm.util.java.JsonUtil;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

class LdapDebugItemGenerator implements DomainItemGenerator
{
    @Override
    public String getFilename()
    {
        return "ldap-servers.json";
    }

    @Override
    public void outputItem( final DomainDebugItemInput debugItemInput, final OutputStream outputStream ) throws Exception
    {
        final List<LdapDebugDataGenerator.LdapDebugInfo> ldapDebugInfos = LdapDebugDataGenerator.makeLdapDebugInfos(
                debugItemInput.getPwmDomain(),
                debugItemInput.getSessionLabel(),
                debugItemInput.getObfuscatedDomainConfig(),
                debugItemInput.getLocale()
        );
        final Writer writer = new OutputStreamWriter( outputStream, PwmConstants.DEFAULT_CHARSET );
        writer.write( JsonUtil.serializeCollection( ldapDebugInfos, JsonUtil.Flag.PrettyPrint ) );
        writer.flush();
    }
}
