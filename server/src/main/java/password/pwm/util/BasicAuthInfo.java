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

package password.pwm.util;

import lombok.Value;
import password.pwm.AppProperty;
import password.pwm.PwmConstants;
import password.pwm.PwmDomain;
import password.pwm.error.PwmUnrecoverableException;
import password.pwm.http.HttpHeader;
import password.pwm.http.PwmRequest;
import password.pwm.util.java.StringUtil;
import password.pwm.util.logging.PwmLogger;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

/**
 * Simple data object containing username/password info derived from a "Basic" Authorization HTTP Header.
 *
 * @author Jason D. Rivard
 */
@Value
public class BasicAuthInfo implements Serializable
{
    private static final PwmLogger LOGGER = PwmLogger.forClass( BasicAuthInfo.class );

    private final String username;
    private final PasswordData password;

    /**
     * Extracts the basic auth info from the header.
     *
     * @param pwmDomain a reference to the application
     * @param pwmRequest http servlet request
     * @return a BasicAuthInfo object containing username/password, or null if the "Authorization" header doesn't exist or is malformed
     */
    public static Optional<BasicAuthInfo> parseAuthHeader(
            final PwmDomain pwmDomain,
            final PwmRequest pwmRequest
    )
    {
        return parseAuthHeader( pwmDomain, pwmRequest.getHttpServletRequest() );
    }

    public static Optional<BasicAuthInfo> parseAuthHeader(
            final PwmDomain pwmDomain,
            final HttpServletRequest httpServletRequest
    )
    {
        final String authHeader = httpServletRequest.getHeader( HttpHeader.Authorization.getHttpName() );

        if ( authHeader != null )
        {
            if ( authHeader.contains( PwmConstants.HTTP_BASIC_AUTH_PREFIX ) )
            {
                // ***** Get the encoded username/chpass string
                // Strip off "Basic " from "Basic c2pvaG5zLmNzaTo=bm92ZWxs"
                final String toStrip = PwmConstants.HTTP_BASIC_AUTH_PREFIX + " ";
                final String encodedValue = authHeader.substring( toStrip.length() );

                try
                {
                    // ***** Decode the username/chpass string
                    final String charSet = pwmDomain.getConfig().readAppProperty( AppProperty.HTTP_BASIC_AUTH_CHARSET );
                    final String decoded = new String( StringUtil.base64Decode( encodedValue ), charSet );

                    // The decoded string should now look something like:
                    //   "cn=user,o=company:chpass" or "user:chpass"
                    return Optional.of( parseHeaderString( decoded ) );
                }
                catch ( final IOException e )
                {
                    LOGGER.debug( () -> "error decoding auth header" + e.getMessage() );
                }
            }
        }

        return Optional.empty();
    }

    public static BasicAuthInfo parseHeaderString( final String input )
    {
        try
        {
            // The decoded string should now look something like:
            //   "cn=user,o=company:chpass" or "user:chpass"

            final int index = input.indexOf( ":" );
            if ( index != -1 )
            {
                // ***** Separate "username:chpass"
                final String username = input.substring( 0, index );
                final PasswordData password = new PasswordData( input.substring( index + 1 ) );
                return new BasicAuthInfo( username, password );
            }
            else
            {
                return new BasicAuthInfo( input, null );
            }
        }
        catch ( final Exception e )
        {
            LOGGER.error( () -> "error decoding auth header: " + e.getMessage() );
            throw new IllegalArgumentException( "invalid basic authentication input string: " + e.getMessage(), e );
        }
    }

    public String toAuthHeader( )
            throws PwmUnrecoverableException
    {
        final StringBuilder sb = new StringBuilder();
        sb.append( this.getUsername() );
        sb.append( ":" );
        sb.append( this.getPassword().getStringValue() );

        sb.replace( 0, sb.length(), StringUtil.base64Encode( sb.toString().getBytes( PwmConstants.DEFAULT_CHARSET ) ) );

        sb.insert( 0, PwmConstants.HTTP_BASIC_AUTH_PREFIX + " " );

        return sb.toString();
    }
}

