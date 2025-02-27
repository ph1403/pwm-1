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

package password.pwm.config;

import lombok.Value;
import password.pwm.PwmConstants;
import password.pwm.config.value.StoredValue;
import password.pwm.util.java.CollectionUtil;
import password.pwm.util.java.StringUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * PwmConfiguration settings.
 *
 * @author Jason D. Rivard
 */
public enum PwmSetting
{
    // templates
    TEMPLATE_LDAP(
            "template.ldap", PwmSettingSyntax.SELECT, PwmSettingCategory.TEMPLATES ),
    TEMPLATE_STORAGE(
            "template.storage", PwmSettingSyntax.SELECT, PwmSettingCategory.TEMPLATES ),

    // notes
    NOTES(
            "notes.noteText", PwmSettingSyntax.TEXT_AREA, PwmSettingCategory.NOTES ),

    // domains
    DOMAIN_LIST(
            "domain.list", PwmSettingSyntax.DOMAIN, PwmSettingCategory.DOMAINS ),
    DOMAIN_SYSTEM_ADMIN(
            "domain.system.adminDomain", PwmSettingSyntax.STRING, PwmSettingCategory.DOMAINS ),
    DOMAIN_DOMAIN_PATHS(
            "domain.system.domainPaths", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.DOMAINS ),

    // application settings
    APP_PROPERTY_OVERRIDES(
            "pwm.appProperty.overrides", PwmSettingSyntax.STRING_ARRAY, PwmSettingCategory.APPLICATION ),
    HIDE_CONFIGURATION_HEALTH_WARNINGS(
            "display.hideConfigHealthWarnings", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.APPLICATION ),

    // domain settings
    PWM_SITE_URL(
            "pwm.selfURL", PwmSettingSyntax.STRING, PwmSettingCategory.APPLICATION ),


    // domain settings
    URL_FORWARD(
            "pwm.forwardURL", PwmSettingSyntax.STRING, PwmSettingCategory.URL_SETTINGS ),
    URL_LOGOUT(
            "pwm.logoutURL", PwmSettingSyntax.STRING, PwmSettingCategory.URL_SETTINGS ),
    URL_HOME(
            "pwm.homeURL", PwmSettingSyntax.STRING, PwmSettingCategory.URL_SETTINGS ),
    URL_INTRO(
            "pwm.introURL", PwmSettingSyntax.SELECT, PwmSettingCategory.URL_SETTINGS ),
    DOMAIN_HOSTS(
            "domain.hosts", PwmSettingSyntax.STRING_ARRAY, PwmSettingCategory.URL_SETTINGS ),

    // telemetry
    PUBLISH_STATS_ENABLE(
            "pwm.publishStats.enable", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.TELEMETRY ),
    PUBLISH_STATS_SITE_DESCRIPTION(
            "pwm.publishStats.siteDescription", PwmSettingSyntax.STRING, PwmSettingCategory.TELEMETRY ),

    KNOWN_LOCALES(
            "knownLocales", PwmSettingSyntax.STRING_ARRAY, PwmSettingCategory.LOCALIZATION ),
    LOCALE_COOKIE_MAX_AGE(
            "locale.cookie.age", PwmSettingSyntax.DURATION, PwmSettingCategory.LOCALIZATION ),

    // clustering
    NODE_SERVICE_ENABLED(
            "nodeService.enable", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.NODES ),
    NODE_SERVICE_STORAGE_MODE(
            "nodeService.storageMode", PwmSettingSyntax.SELECT, PwmSettingCategory.NODES ),
    SECURITY_LOGIN_SESSION_MODE(
            "security.loginSession.mode", PwmSettingSyntax.SELECT, PwmSettingCategory.NODES ),
    SECURITY_MODULE_SESSION_MODE(
            "security.moduleSession.mode", PwmSettingSyntax.SELECT, PwmSettingCategory.NODES ),

    // user interface
    INTERFACE_THEME(
            "interface.theme", PwmSettingSyntax.SELECT, PwmSettingCategory.UI_WEB ),
    DISPLAY_SHOW_HIDE_PASSWORD_FIELDS(
            "display.showHidePasswordFields", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.UI_FEATURES ),
    DISPLAY_MASK_PASSWORD_FIELDS(
            "display.maskPasswordFields", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.UI_FEATURES ),
    DISPLAY_MASK_RESPONSE_FIELDS(
            "display.maskResponseFields", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.UI_FEATURES ),
    DISPLAY_MASK_TOKEN_FIELDS(
            "display.maskTokenFields", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.UI_FEATURES ),
    DISPLAY_CANCEL_BUTTON(
            "display.showCancelButton", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.UI_FEATURES ),
    DISPLAY_TOKEN_SUCCESS_BUTTON(
            "display.tokenSuccessPage", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.UI_FEATURES ),
    DISPLAY_SUCCESS_PAGES(
            "display.showSuccessPage", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.UI_FEATURES ),
    DISPLAY_LOGIN_PAGE_OPTIONS(
            "display.showLoginPageOptions", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.UI_FEATURES ),
    DISPLAY_LOGOUT_BUTTON(
            "display.logoutButton", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.UI_FEATURES ),
    DISPLAY_HOME_BUTTON(
            "display.homeButton", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.UI_FEATURES ),
    DISPLAY_IDLE_TIMEOUT(
            "display.idleTimeout", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.UI_FEATURES ),
    PASSWORD_SHOW_STRENGTH_METER(
            "password.showStrengthMeter", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.UI_FEATURES ),
    IDLE_TIMEOUT_SECONDS(
            "idleTimeoutSeconds", PwmSettingSyntax.DURATION, PwmSettingCategory.UI_FEATURES ),
    DISPLAY_CSS_CUSTOM_STYLE(
            "display.css.customStyleLocation", PwmSettingSyntax.STRING, PwmSettingCategory.UI_WEB ),
    DISPLAY_CSS_CUSTOM_MOBILE_STYLE(
            "display.css.customMobileStyleLocation", PwmSettingSyntax.STRING, PwmSettingCategory.UI_WEB ),
    DISPLAY_CSS_EMBED(
            "display.css.customStyle", PwmSettingSyntax.TEXT_AREA, PwmSettingCategory.UI_WEB ),
    DISPLAY_CSS_MOBILE_EMBED(
            "display.css.customMobileStyle", PwmSettingSyntax.TEXT_AREA, PwmSettingCategory.UI_WEB ),
    DISPLAY_CUSTOM_JAVASCRIPT(
            "display.js.custom", PwmSettingSyntax.TEXT_AREA, PwmSettingCategory.UI_WEB ),
    DISPLAY_CUSTOM_RESOURCE_BUNDLE(
            "display.custom.resourceBundle", PwmSettingSyntax.FILE, PwmSettingCategory.UI_WEB ),

    // change password
    CHANGE_PASSWORD_PROFILE_LIST(
            "changePassword.profile.list", PwmSettingSyntax.PROFILE, PwmSettingCategory.INTERNAL_DOMAIN ),
    CHANGE_PASSWORD_ENABLE(
            "changePassword.enable", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.CHANGE_PASSWORD_SETTINGS ),
    QUERY_MATCH_CHANGE_PASSWORD(
            "password.allowChange.queryMatch", PwmSettingSyntax.USER_PERMISSION, PwmSettingCategory.CHANGE_PASSWORD_PROFILE ),
    LOGOUT_AFTER_PASSWORD_CHANGE(
            "logoutAfterPasswordChange", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.CHANGE_PASSWORD_PROFILE ),
    PASSWORD_REQUIRE_FORM(
            "password.require.form", PwmSettingSyntax.FORM, PwmSettingCategory.CHANGE_PASSWORD_PROFILE ),
    PASSWORD_REQUIRE_CURRENT(
            "password.change.requireCurrent", PwmSettingSyntax.SELECT, PwmSettingCategory.CHANGE_PASSWORD_PROFILE ),
    PASSWORD_CHANGE_AGREEMENT_MESSAGE(
            "display.password.changeAgreement", PwmSettingSyntax.LOCALIZED_TEXT_AREA, PwmSettingCategory.CHANGE_PASSWORD_PROFILE ),
    PASSWORD_COMPLETE_MESSAGE(
            "display.password.completeMessage", PwmSettingSyntax.LOCALIZED_TEXT_AREA, PwmSettingCategory.CHANGE_PASSWORD_PROFILE ),
    DISPLAY_PASSWORD_GUIDE_TEXT(
            "display.password.guideText", PwmSettingSyntax.LOCALIZED_TEXT_AREA, PwmSettingCategory.CHANGE_PASSWORD_PROFILE ),
    PASSWORD_SYNC_MIN_WAIT_TIME(
            "passwordSyncMinWaitTime", PwmSettingSyntax.DURATION, PwmSettingCategory.CHANGE_PASSWORD_PROFILE ),
    PASSWORD_SYNC_MAX_WAIT_TIME(
            "passwordSyncMaxWaitTime", PwmSettingSyntax.DURATION, PwmSettingCategory.CHANGE_PASSWORD_PROFILE ),
    PASSWORD_EXPIRE_PRE_TIME(
            "expirePreTime", PwmSettingSyntax.DURATION, PwmSettingCategory.CHANGE_PASSWORD_PROFILE ),
    PASSWORD_EXPIRE_WARN_TIME(
            "expireWarnTime", PwmSettingSyntax.DURATION, PwmSettingCategory.CHANGE_PASSWORD_PROFILE ),
    EXPIRE_CHECK_DURING_AUTH(
            "expireCheckDuringAuth", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.CHANGE_PASSWORD_PROFILE ),
    CHANGE_PASSWORD_WRITE_ATTRIBUTES(
            "changePassword.writeAttributes", PwmSettingSyntax.ACTION, PwmSettingCategory.CHANGE_PASSWORD_PROFILE ),
    PASSWORD_SHOW_AUTOGEN(
            "password.showAutoGen", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.CHANGE_PASSWORD_PROFILE ),

    // account info
    ACCOUNT_INFORMATION_ENABLED(
            "display.accountInformation", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.ACCOUNT_INFO_SETTINGS ),
    ACCOUNT_INFORMATION_PROFILE_LIST(
            "accountInfo.profile.list", PwmSettingSyntax.PROFILE, PwmSettingCategory.INTERNAL_DOMAIN ),
    ACCOUNT_INFORMATION_QUERY_MATCH(
            "accountInfo.queryMatch", PwmSettingSyntax.USER_PERMISSION, PwmSettingCategory.ACCOUNT_INFO_PROFILE ),
    ACCOUNT_INFORMATION_HISTORY(
            "display.passwordHistory", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.ACCOUNT_INFO_PROFILE ),
    ACCOUNT_INFORMATION_VIEW_STATUS_VALUES(
            "accountInfo.viewStatusValues", PwmSettingSyntax.OPTIONLIST, PwmSettingCategory.ACCOUNT_INFO_PROFILE ),
    ACCOUNT_INFORMATION_VIEW_FORM(
            "accountInfo.view.form", PwmSettingSyntax.FORM, PwmSettingCategory.ACCOUNT_INFO_PROFILE ),


    // delete info
    DELETE_ACCOUNT_PROFILE_LIST(
            "deleteAccount.profile.list", PwmSettingSyntax.PROFILE, PwmSettingCategory.INTERNAL_DOMAIN ),
    DELETE_ACCOUNT_ENABLE(
            "deleteAccount.enable", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.DELETE_ACCOUNT_SETTINGS ),
    DELETE_ACCOUNT_PERMISSION(
            "deleteAccount.permission", PwmSettingSyntax.USER_PERMISSION, PwmSettingCategory.DELETE_ACCOUNT_PROFILE ),
    DELETE_ACCOUNT_AGREEMENT(
            "deleteAccount.agreement", PwmSettingSyntax.LOCALIZED_TEXT_AREA, PwmSettingCategory.DELETE_ACCOUNT_PROFILE ),
    DELETE_ACCOUNT_DELETE_USER_ENTRY(
            "deleteAccount.deleteEntry", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.DELETE_ACCOUNT_PROFILE ),
    DELETE_ACCOUNT_ACTIONS(
            "deleteAccount.actions", PwmSettingSyntax.ACTION, PwmSettingCategory.DELETE_ACCOUNT_PROFILE ),
    DELETE_ACCOUNT_NEXT_URL(
            "deleteAccount.nextUrl", PwmSettingSyntax.STRING, PwmSettingCategory.DELETE_ACCOUNT_PROFILE ),


    //ldap directories
    LDAP_SERVER_URLS(
            "ldap.serverUrls", PwmSettingSyntax.STRING_ARRAY, PwmSettingCategory.LDAP_BASE ),
    LDAP_SERVER_CERTS(
            "ldap.serverCerts", PwmSettingSyntax.X509CERT, PwmSettingCategory.LDAP_BASE ),
    LDAP_PROXY_USER_DN(
            "ldap.proxy.username", PwmSettingSyntax.STRING, PwmSettingCategory.LDAP_BASE ),
    LDAP_PROXY_USER_PASSWORD(
            "ldap.proxy.password", PwmSettingSyntax.PASSWORD, PwmSettingCategory.LDAP_BASE ),
    LDAP_CONTEXTLESS_ROOT(
            "ldap.rootContexts", PwmSettingSyntax.STRING_ARRAY, PwmSettingCategory.LDAP_BASE ),
    LDAP_TEST_USER_DN(
            "ldap.testuser.username", PwmSettingSyntax.STRING, PwmSettingCategory.LDAP_BASE ),
    LDAP_GUID_AUTO_ADD(
            "ldap.guid.autoAddValue", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.LDAP_BASE ),
    LDAP_SEARCH_TIMEOUT(
            "ldap.search.timeoutSeconds", PwmSettingSyntax.DURATION, PwmSettingCategory.LDAP_BASE ),
    LDAP_PROFILE_ENABLED(
            "ldap.profile.enabled", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.LDAP_BASE ),

    LDAP_USERNAME_SEARCH_FILTER(
            "ldap.usernameSearchFilter", PwmSettingSyntax.STRING, PwmSettingCategory.LDAP_LOGIN ),
    LDAP_LOGIN_CONTEXTS(
            "ldap.selectableContexts", PwmSettingSyntax.STRING_ARRAY, PwmSettingCategory.LDAP_LOGIN ),
    LDAP_PROFILE_DISPLAY_NAME(
            "ldap.profile.displayName", PwmSettingSyntax.LOCALIZED_STRING, PwmSettingCategory.LDAP_LOGIN ),

    // ldap attributes
    LDAP_USERNAME_ATTRIBUTE(
            "ldap.username.attr", PwmSettingSyntax.STRING, PwmSettingCategory.LDAP_ATTRIBUTES ),
    LDAP_GUID_ATTRIBUTE(
            "ldap.guidAttribute", PwmSettingSyntax.STRING, PwmSettingCategory.LDAP_ATTRIBUTES ),
    LDAP_NAMING_ATTRIBUTE(
            "ldap.namingAttribute", PwmSettingSyntax.STRING, PwmSettingCategory.LDAP_ATTRIBUTES ),
    PASSWORD_LAST_UPDATE_ATTRIBUTE(
            "passwordLastUpdateAttribute", PwmSettingSyntax.STRING, PwmSettingCategory.LDAP_ATTRIBUTES ),
    LDAP_USER_GROUP_ATTRIBUTE(
            "ldap.user.group.attribute", PwmSettingSyntax.STRING, PwmSettingCategory.LDAP_ATTRIBUTES ),
    LDAP_GROUP_LABEL_ATTRIBUTE(
            "ldap.group.label.attribute", PwmSettingSyntax.STRING, PwmSettingCategory.LDAP_ATTRIBUTES ),
    EMAIL_USER_MAIL_ATTRIBUTE(
            "email.userMailAttribute", PwmSettingSyntax.STRING, PwmSettingCategory.LDAP_ATTRIBUTES ),
    EMAIL_USER_MAIL_ATTRIBUTE_2(
            "email.userMailAttribute2", PwmSettingSyntax.STRING, PwmSettingCategory.LDAP_ATTRIBUTES ),
    EMAIL_USER_MAIL_ATTRIBUTE_3(
            "email.userMailAttribute3", PwmSettingSyntax.STRING, PwmSettingCategory.LDAP_ATTRIBUTES ),
    SMS_USER_PHONE_ATTRIBUTE(
            "sms.userSmsAttribute", PwmSettingSyntax.STRING, PwmSettingCategory.LDAP_ATTRIBUTES ),
    SMS_USER_PHONE_ATTRIBUTE_2(
            "sms.userSmsAttribute2", PwmSettingSyntax.STRING, PwmSettingCategory.LDAP_ATTRIBUTES ),
    SMS_USER_PHONE_ATTRIBUTE_3(
            "sms.userSmsAttribute3", PwmSettingSyntax.STRING, PwmSettingCategory.LDAP_ATTRIBUTES ),
    CHALLENGE_USER_ATTRIBUTE(
            "challenge.userAttribute", PwmSettingSyntax.STRING, PwmSettingCategory.LDAP_ATTRIBUTES ),
    EVENTS_LDAP_ATTRIBUTE(
            "events.ldap.attribute", PwmSettingSyntax.STRING, PwmSettingCategory.LDAP_ATTRIBUTES ),
    CACHED_USER_ATTRIBUTES(
            "webservice.userAttributes", PwmSettingSyntax.STRING_ARRAY, PwmSettingCategory.LDAP_ATTRIBUTES ),
    OTP_SECRET_LDAP_ATTRIBUTE(
            "otp.secret.ldap.attribute", PwmSettingSyntax.STRING, PwmSettingCategory.LDAP_ATTRIBUTES ),
    LDAP_ATTRIBUTE_PHOTO(
            "peopleSearch.photo.ldapAttribute", PwmSettingSyntax.STRING, PwmSettingCategory.LDAP_ATTRIBUTES ),
    LDAP_ATTRIBUTE_PHOTO_URL_OVERRIDE(
            "peopleSearch.photo.urlOverride", PwmSettingSyntax.STRING, PwmSettingCategory.LDAP_ATTRIBUTES ),
    LDAP_ATTRIBUTE_ORGCHART_PARENT(
            "peopleSearch.orgChart.parentAttribute", PwmSettingSyntax.STRING, PwmSettingCategory.LDAP_ATTRIBUTES ),
    LDAP_ATTRIBUTE_ORGCHART_CHILD(
            "peopleSearch.orgChart.childAttribute", PwmSettingSyntax.STRING, PwmSettingCategory.LDAP_ATTRIBUTES ),
    LDAP_ATTRIBUTE_ORGCHART_ASSISTANT(
            "peopleSearch.orgChart.assistantAttribute", PwmSettingSyntax.STRING, PwmSettingCategory.LDAP_ATTRIBUTES ),
    LDAP_ATTRIBUTE_ORGCHART_WORKFORCEID(
            "peopleSearch.orgChart.workforceIdAttribute", PwmSettingSyntax.STRING, PwmSettingCategory.LDAP_ATTRIBUTES ),
    LDAP_ATTRIBUTE_LANGUAGE(
            "ldap.user.language.attribute", PwmSettingSyntax.STRING, PwmSettingCategory.LDAP_ATTRIBUTES ),
    LDAP_ATTRIBUTE_PWNOTIFY(
            "ldap.user.appData.attribute", PwmSettingSyntax.STRING, PwmSettingCategory.LDAP_ATTRIBUTES ),
    LDAP_AUTO_SET_LANGUAGE_VALUE(
            "ldap.user.language.autoSet", PwmSettingSyntax.SELECT, PwmSettingCategory.LDAP_ATTRIBUTES ),
    AUTO_ADD_OBJECT_CLASSES(
            "ldap.addObjectClasses", PwmSettingSyntax.STRING_ARRAY, PwmSettingCategory.LDAP_ATTRIBUTES ),

    // ldap global settings
    LDAP_PROFILE_LIST(
            "ldap.profile.list", PwmSettingSyntax.PROFILE, PwmSettingCategory.INTERNAL_DOMAIN ),
    LDAP_IDLE_TIMEOUT(
            "ldap.idleTimeout", PwmSettingSyntax.DURATION, PwmSettingCategory.LDAP_GLOBAL ),
    DEFAULT_OBJECT_CLASSES(
            "ldap.defaultObjectClasses", PwmSettingSyntax.STRING_ARRAY, PwmSettingCategory.LDAP_GLOBAL ),
    LDAP_FOLLOW_REFERRALS(
            "ldap.followReferrals", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.LDAP_GLOBAL ),
    LDAP_DUPLICATE_MODE(
            "ldap.duplicateMode", PwmSettingSyntax.SELECT, PwmSettingCategory.LDAP_GLOBAL ),
    LDAP_SELECTABLE_CONTEXT_MODE(
            "ldap.selectableContextMode", PwmSettingSyntax.SELECT, PwmSettingCategory.LDAP_GLOBAL ),
    LDAP_IGNORE_UNREACHABLE_PROFILES(
            "ldap.ignoreUnreachableProfiles", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.LDAP_GLOBAL ),
    LDAP_ENABLE_WIRE_TRACE(
            "ldap.wireTrace.enable", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.LDAP_GLOBAL ),
    PASSWORD_SYNC_ENABLE_REPLICA_CHECK(
            "passwordSync.enableReplicaCheck", PwmSettingSyntax.SELECT, PwmSettingCategory.LDAP_GLOBAL ),


    // New multiple email settings
    EMAIL_SERVERS(
            "email.profile.list", PwmSettingSyntax.PROFILE, PwmSettingCategory.INTERNAL_SYSTEM ),
    EMAIL_SERVER_ADDRESS(
            "email.smtp.address", PwmSettingSyntax.STRING, PwmSettingCategory.EMAIL_SERVERS ),
    EMAIL_SERVER_TYPE(
            "email.smtp.type", PwmSettingSyntax.SELECT, PwmSettingCategory.EMAIL_SERVERS ),
    EMAIL_SERVER_PORT(
            "email.smtp.port", PwmSettingSyntax.NUMERIC, PwmSettingCategory.EMAIL_SERVERS ),
    EMAIL_SERVER_CERTS(
            "email.smtp.serverCerts", PwmSettingSyntax.X509CERT, PwmSettingCategory.EMAIL_SERVERS ),
    EMAIL_USERNAME(
            "email.smtp.username", PwmSettingSyntax.STRING, PwmSettingCategory.EMAIL_SERVERS ),
    EMAIL_PASSWORD(
            "email.smtp.userpassword", PwmSettingSyntax.PASSWORD, PwmSettingCategory.EMAIL_SERVERS ),

    // system wide email settings
    EMAIL_MAX_QUEUE_AGE(
            "email.queueMaxAge", PwmSettingSyntax.DURATION, PwmSettingCategory.EMAIL_SETTINGS ),
    EMAIL_ADVANCED_SETTINGS(
            "email.smtp.advancedSettings", PwmSettingSyntax.STRING_ARRAY, PwmSettingCategory.EMAIL_SETTINGS ),
    EMAIL_SYSTEM_FROM_ADDRESS(
            "email.system.fromAddress", PwmSettingSyntax.STRING, PwmSettingCategory.EMAIL_SETTINGS ),

    // email template
    EMAIL_DOMAIN_FROM_ADDRESS(
            "email.default.fromAddress", PwmSettingSyntax.STRING, PwmSettingCategory.EMAIL_TEMPLATES ),
    EMAIL_CHANGEPASSWORD(
            "email.changePassword", PwmSettingSyntax.EMAIL, PwmSettingCategory.EMAIL_TEMPLATES ),
    EMAIL_CHANGEPASSWORD_HELPDESK(
            "email.changePassword.helpdesk", PwmSettingSyntax.EMAIL, PwmSettingCategory.EMAIL_TEMPLATES ),
    EMAIL_UPDATEPROFILE(
            "email.updateProfile", PwmSettingSyntax.EMAIL, PwmSettingCategory.EMAIL_TEMPLATES ),
    EMAIL_UPDATEPROFILE_VERIFICATION(
            "email.updateProfile.token", PwmSettingSyntax.EMAIL, PwmSettingCategory.EMAIL_TEMPLATES ),
    EMAIL_NEWUSER(
            "email.newUser", PwmSettingSyntax.EMAIL, PwmSettingCategory.EMAIL_TEMPLATES ),
    EMAIL_NEWUSER_VERIFICATION(
            "email.newUser.token", PwmSettingSyntax.EMAIL, PwmSettingCategory.EMAIL_TEMPLATES ),
    EMAIL_ACTIVATION(
            "email.activation", PwmSettingSyntax.EMAIL, PwmSettingCategory.EMAIL_TEMPLATES ),
    EMAIL_ACTIVATION_VERIFICATION(
            "email.activation.token", PwmSettingSyntax.EMAIL, PwmSettingCategory.EMAIL_TEMPLATES ),
    EMAIL_CHALLENGE_TOKEN(
            "email.challenge.token", PwmSettingSyntax.EMAIL, PwmSettingCategory.EMAIL_TEMPLATES ),
    EMAIL_HELPDESK_TOKEN(
            "email.helpdesk.token", PwmSettingSyntax.EMAIL, PwmSettingCategory.EMAIL_TEMPLATES ),
    EMAIL_GUEST(
            "email.guest", PwmSettingSyntax.EMAIL, PwmSettingCategory.EMAIL_TEMPLATES ),
    EMAIL_UPDATEGUEST(
            "email.updateguest", PwmSettingSyntax.EMAIL, PwmSettingCategory.EMAIL_TEMPLATES ),
    EMAIL_SENDPASSWORD(
            "email.sendpassword", PwmSettingSyntax.EMAIL, PwmSettingCategory.EMAIL_TEMPLATES ),
    EMAIL_SEND_USERNAME(
            "email.sendUsername", PwmSettingSyntax.EMAIL, PwmSettingCategory.EMAIL_TEMPLATES ),
    EMAIL_INTRUDERNOTICE(
            "email.intruderNotice", PwmSettingSyntax.EMAIL, PwmSettingCategory.EMAIL_TEMPLATES ),
    EMAIL_DELETEACCOUNT(
            "email.deleteAccount", PwmSettingSyntax.EMAIL, PwmSettingCategory.EMAIL_TEMPLATES ),
    EMAIL_HELPDESK_UNLOCK(
            "email.helpdesk.unlock", PwmSettingSyntax.EMAIL, PwmSettingCategory.EMAIL_TEMPLATES ),
    EMAIL_UNLOCK(
            "email.unlock", PwmSettingSyntax.EMAIL, PwmSettingCategory.EMAIL_TEMPLATES ),
    EMAIL_PW_EXPIRATION_NOTICE(
            "email.pwNotice", PwmSettingSyntax.EMAIL, PwmSettingCategory.EMAIL_TEMPLATES ),


    // sms settings
    SMS_GATEWAY_URL(
            "sms.gatewayURL", PwmSettingSyntax.STRING, PwmSettingCategory.SMS_GATEWAY ),
    SMS_GATEWAY_CERTIFICATES(
            "sms.gatewayCertificates", PwmSettingSyntax.X509CERT, PwmSettingCategory.SMS_GATEWAY ),
    SMS_GATEWAY_METHOD(
            "sms.gatewayMethod", PwmSettingSyntax.SELECT, PwmSettingCategory.SMS_GATEWAY ),
    SMS_GATEWAY_USER(
            "sms.gatewayUser", PwmSettingSyntax.STRING, PwmSettingCategory.SMS_GATEWAY ),
    SMS_GATEWAY_PASSWORD(
            "sms.gatewayPassword", PwmSettingSyntax.PASSWORD, PwmSettingCategory.SMS_GATEWAY ),
    SMS_GATEWAY_AUTHMETHOD(
            "sms.gatewayAuthMethod", PwmSettingSyntax.SELECT, PwmSettingCategory.SMS_GATEWAY ),
    SMS_REQUEST_DATA(
            "sms.requestData", PwmSettingSyntax.TEXT_AREA, PwmSettingCategory.SMS_GATEWAY ),
    SMS_REQUEST_CONTENT_TYPE(
            "sms.requestContentType", PwmSettingSyntax.STRING, PwmSettingCategory.SMS_GATEWAY ),
    SMS_REQUEST_CONTENT_ENCODING(
            "sms.requestContentEncoding", PwmSettingSyntax.SELECT, PwmSettingCategory.SMS_GATEWAY ),
    SMS_GATEWAY_REQUEST_HEADERS(
            "sms.httpRequestHeaders", PwmSettingSyntax.STRING_ARRAY, PwmSettingCategory.SMS_GATEWAY ),
    SMS_MAX_TEXT_LENGTH(
            "sms.maxTextLength", PwmSettingSyntax.NUMERIC, PwmSettingCategory.SMS_GATEWAY ),
    SMS_SENDER_ID(
            "sms.senderID", PwmSettingSyntax.STRING, PwmSettingCategory.SMS_GATEWAY ),
    SMS_PHONE_NUMBER_FORMAT(
            "sms.phoneNumberFormat", PwmSettingSyntax.SELECT, PwmSettingCategory.SMS_GATEWAY ),
    SMS_DEFAULT_COUNTRY_CODE(
            "sms.defaultCountryCode", PwmSettingSyntax.NUMERIC, PwmSettingCategory.SMS_GATEWAY ),
    SMS_REQUESTID_CHARS(
            "sms.requestId.characters", PwmSettingSyntax.STRING, PwmSettingCategory.SMS_GATEWAY ),
    SMS_REQUESTID_LENGTH(
            "sms.requestId.length", PwmSettingSyntax.NUMERIC, PwmSettingCategory.SMS_GATEWAY ),
    SMS_USE_URL_SHORTENER(
            "sms.useUrlShortener", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.SMS_GATEWAY ),
    SMS_RESPONSE_OK_REGEX(
            "sms.responseOkRegex", PwmSettingSyntax.STRING_ARRAY, PwmSettingCategory.SMS_GATEWAY ),
    SMS_SUCCESS_RESULT_CODE(
            "sms.successResultCodes", PwmSettingSyntax.STRING_ARRAY, PwmSettingCategory.SMS_GATEWAY ),
    URL_SHORTENER_CLASS(
            "urlshortener.classname", PwmSettingSyntax.STRING, PwmSettingCategory.SMS_GATEWAY ),
    URL_SHORTENER_PARAMETERS(
            "urlshortener.parameters", PwmSettingSyntax.STRING_ARRAY, PwmSettingCategory.SMS_GATEWAY ),
    SMS_MAX_QUEUE_AGE(
            "sms.queueMaxAge", PwmSettingSyntax.DURATION, PwmSettingCategory.SMS_GATEWAY ),

    SMS_CHALLENGE_TOKEN_TEXT(
            "sms.challenge.token.message", PwmSettingSyntax.LOCALIZED_STRING, PwmSettingCategory.SMS_MESSAGES ),
    SMS_CHALLENGE_NEW_PASSWORD_TEXT(
            "sms.challenge.newpassword.message", PwmSettingSyntax.LOCALIZED_STRING, PwmSettingCategory.SMS_MESSAGES ),
    SMS_NEWUSER_TOKEN_TEXT(
            "sms.newUser.token.message", PwmSettingSyntax.LOCALIZED_STRING, PwmSettingCategory.SMS_MESSAGES ),
    SMS_HELPDESK_TOKEN_TEXT(
            "sms.helpdesk.token.message", PwmSettingSyntax.LOCALIZED_STRING, PwmSettingCategory.SMS_MESSAGES ),
    SMS_ACTIVATION_VERIFICATION_TEXT(
            "sms.activation.token.message", PwmSettingSyntax.LOCALIZED_STRING, PwmSettingCategory.SMS_MESSAGES ),
    SMS_ACTIVATION_TEXT(
            "sms.activation.message", PwmSettingSyntax.LOCALIZED_STRING, PwmSettingCategory.SMS_MESSAGES ),
    SMS_FORGOTTEN_USERNAME_TEXT(
            "sms.forgottenUsername.message", PwmSettingSyntax.LOCALIZED_STRING, PwmSettingCategory.SMS_MESSAGES ),
    SMS_UPDATE_PROFILE_TOKEN_TEXT(
            "sms.updateProfile.token.message", PwmSettingSyntax.LOCALIZED_STRING, PwmSettingCategory.SMS_MESSAGES ),


    //global password policy settings
    PASSWORD_POLICY_SOURCE(
            "password.policy.source", PwmSettingSyntax.SELECT, PwmSettingCategory.PASSWORD_GLOBAL ),
    PASSWORD_SHAREDHISTORY_ENABLE(
            "password.sharedHistory.enable", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.PASSWORD_GLOBAL ),
    PASSWORD_SHAREDHISTORY_MAX_AGE(
            "password.sharedHistory.age", PwmSettingSyntax.DURATION, PwmSettingCategory.PASSWORD_GLOBAL ),
    PASSWORD_POLICY_CASE_SENSITIVITY(
            "password.policy.caseSensitivity", PwmSettingSyntax.SELECT, PwmSettingCategory.PASSWORD_GLOBAL ),
    PASSWORD_PROFILE_LIST(
            "password.profile.list", PwmSettingSyntax.PROFILE, PwmSettingCategory.INTERNAL_DOMAIN ),


    // wordlist settings
    WORDLIST_FILENAME(
            "pwm.wordlist.location", PwmSettingSyntax.STRING, PwmSettingCategory.WORDLISTS ),
    WORDLIST_CASE_SENSITIVE(
            "wordlistCaseSensitive", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.WORDLISTS ),
    PASSWORD_WORDLIST_WORDSIZE(
            "password.wordlist.wordSize", PwmSettingSyntax.NUMERIC, PwmSettingCategory.WORDLISTS ),
    SEEDLIST_FILENAME(
            "pwm.seedlist.location", PwmSettingSyntax.STRING, PwmSettingCategory.WORDLISTS ),


    // password policy profile settings
    PASSWORD_POLICY_QUERY_MATCH(
            "password.policy.queryMatch", PwmSettingSyntax.USER_PERMISSION, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_MINIMUM_LENGTH(
            "password.policy.minimumLength", PwmSettingSyntax.NUMERIC, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_MAXIMUM_LENGTH(
            "password.policy.maximumLength", PwmSettingSyntax.NUMERIC, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_MAXIMUM_REPEAT(
            "password.policy.maximumRepeat", PwmSettingSyntax.NUMERIC, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_MAXIMUM_SEQUENTIAL_REPEAT(
            "password.policy.maximumSequentialRepeat", PwmSettingSyntax.NUMERIC, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_ALLOW_NUMERIC(
            "password.policy.allowNumeric", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_ALLOW_FIRST_CHAR_NUMERIC(
            "password.policy.allowFirstCharNumeric", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_ALLOW_LAST_CHAR_NUMERIC(
            "password.policy.allowLastCharNumeric", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_MAXIMUM_NUMERIC(
            "password.policy.maximumNumeric", PwmSettingSyntax.NUMERIC, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_MINIMUM_NUMERIC(
            "password.policy.minimumNumeric", PwmSettingSyntax.NUMERIC, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_ALLOW_SPECIAL(
            "password.policy.allowSpecial", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_ALLOW_FIRST_CHAR_SPECIAL(
            "password.policy.allowFirstCharSpecial", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_ALLOW_LAST_CHAR_SPECIAL(
            "password.policy.allowLastCharSpecial", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_MAXIMUM_SPECIAL(
            "password.policy.maximumSpecial", PwmSettingSyntax.NUMERIC, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_MINIMUM_SPECIAL(
            "password.policy.minimumSpecial", PwmSettingSyntax.NUMERIC, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_MAXIMUM_ALPHA(
            "password.policy.maximumAlpha", PwmSettingSyntax.NUMERIC, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_MINIMUM_ALPHA(
            "password.policy.minimumAlpha", PwmSettingSyntax.NUMERIC, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_ALLOW_NON_ALPHA(
            "password.policy.allowNonAlpha", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_MAXIMUM_NON_ALPHA(
            "password.policy.maximumNonAlpha", PwmSettingSyntax.NUMERIC, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_MINIMUM_NON_ALPHA(
            "password.policy.minimumNonAlpha", PwmSettingSyntax.NUMERIC, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_MAXIMUM_UPPERCASE(
            "password.policy.maximumUpperCase", PwmSettingSyntax.NUMERIC, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_MINIMUM_UPPERCASE(
            "password.policy.minimumUpperCase", PwmSettingSyntax.NUMERIC, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_MAXIMUM_LOWERCASE(
            "password.policy.maximumLowerCase", PwmSettingSyntax.NUMERIC, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_MINIMUM_LOWERCASE(
            "password.policy.minimumLowerCase", PwmSettingSyntax.NUMERIC, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_MINIMUM_UNIQUE(
            "password.policy.minimumUnique", PwmSettingSyntax.NUMERIC, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_MAXIMUM_OLD_PASSWORD_CHARS(
            "password.policy.maximumOldPasswordChars", PwmSettingSyntax.NUMERIC, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_MINIMUM_LIFETIME(
            "password.policy.minimumLifetime", PwmSettingSyntax.DURATION, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_MAXIMUM_CONSECUTIVE(
            "password.policy.maximumConsecutive", PwmSettingSyntax.NUMERIC, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_MINIMUM_STRENGTH(
            "password.policy.minimumStrength", PwmSettingSyntax.NUMERIC, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_ENABLE_WORDLIST(
            "password.policy.checkWordlist", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_AD_COMPLEXITY_LEVEL(
            "password.policy.ADComplexityLevel", PwmSettingSyntax.SELECT, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_AD_COMPLEXITY_MAX_VIOLATIONS(
            "password.policy.ADComplexityMaxViolations", PwmSettingSyntax.NUMERIC, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_REGULAR_EXPRESSION_MATCH(
            "password.policy.regExMatch", PwmSettingSyntax.STRING_ARRAY, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_REGULAR_EXPRESSION_NOMATCH(
            "password.policy.regExNoMatch", PwmSettingSyntax.STRING_ARRAY, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_DISALLOWED_VALUES(
            "password.policy.disallowedValues", PwmSettingSyntax.STRING_ARRAY, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_DISALLOWED_ATTRIBUTES(
            "password.policy.disallowedAttributes", PwmSettingSyntax.STRING_ARRAY, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_CHANGE_MESSAGE(
            "password.policy.changeMessage", PwmSettingSyntax.LOCALIZED_TEXT_AREA, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_RULE_TEXT(
            "password.policy.ruleText", PwmSettingSyntax.LOCALIZED_TEXT_AREA, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_DISALLOW_CURRENT(
            "password.policy.disallowCurrent", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_CHAR_GROUPS_MIN_MATCH(
            "password.policy.charGroup.minimumMatch", PwmSettingSyntax.NUMERIC, PwmSettingCategory.PASSWORD_POLICY ),
    PASSWORD_POLICY_CHAR_GROUPS(
            "password.policy.charGroup.regExValues", PwmSettingSyntax.STRING_ARRAY, PwmSettingCategory.PASSWORD_POLICY ),


    // app security settings
    PWM_SECURITY_KEY(
            "pwm.securityKey", PwmSettingSyntax.PASSWORD, PwmSettingCategory.APP_SECURITY ),
    REVERSE_DNS_ENABLE(
            "network.reverseDNS.enable", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.APP_SECURITY ),
    DISPLAY_SHOW_DETAILED_ERRORS(
            "display.showDetailedErrors", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.APP_SECURITY ),
    SESSION_MAX_SECONDS(
            "session.maxSeconds", PwmSettingSyntax.DURATION, PwmSettingCategory.APP_SECURITY ),
    CERTIFICATE_VALIDATION_MODE(
            "security.certificate.validationMode", PwmSettingSyntax.SELECT, PwmSettingCategory.APP_SECURITY ),

    // web security
    SECURITY_ENABLE_FORM_NONCE(
            "security.formNonce.enable", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.WEB_SECURITY ),
    ENABLE_SESSION_VERIFICATION(
            "enableSessionVerification", PwmSettingSyntax.SELECT, PwmSettingCategory.WEB_SECURITY ),
    DISALLOWED_HTTP_INPUTS(
            "disallowedInputs", PwmSettingSyntax.STRING_ARRAY, PwmSettingCategory.WEB_SECURITY ),
    USE_X_FORWARDED_FOR_HEADER(
            "useXForwardedForHeader", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.WEB_SECURITY ),
    MULTI_IP_SESSION_ALLOWED(
            "network.allowMultiIPSession", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.WEB_SECURITY ),
    REQUIRED_HEADERS(
            "network.requiredHttpHeaders", PwmSettingSyntax.STRING_ARRAY, PwmSettingCategory.WEB_SECURITY ),
    IP_PERMITTED_RANGE(
            "network.ip.permittedRange", PwmSettingSyntax.STRING_ARRAY, PwmSettingCategory.WEB_SECURITY ),
    SECURITY_PAGE_LEAVE_NOTICE_TIMEOUT(
            "security.page.leaveNoticeTimeout", PwmSettingSyntax.NUMERIC, PwmSettingCategory.WEB_SECURITY ),
    SECURITY_PREVENT_FRAMING(
            "security.preventFraming", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.WEB_SECURITY ),
    SECURITY_REDIRECT_WHITELIST(
            "security.redirectUrl.whiteList", PwmSettingSyntax.STRING_ARRAY, PwmSettingCategory.WEB_SECURITY ),
    SECURITY_CSP_HEADER(
            "security.cspHeader", PwmSettingSyntax.STRING, PwmSettingCategory.WEB_SECURITY ),

    // catpcha
    RECAPTCHA_KEY_PUBLIC(
            "captcha.recaptcha.publicKey", PwmSettingSyntax.STRING, PwmSettingCategory.CAPTCHA ),
    RECAPTCHA_KEY_PRIVATE(
            "captcha.recaptcha.privateKey", PwmSettingSyntax.PASSWORD, PwmSettingCategory.CAPTCHA ),
    CAPTCHA_PROTECTED_PAGES(
            "captcha.protectedPages", PwmSettingSyntax.OPTIONLIST, PwmSettingCategory.CAPTCHA ),
    CAPTCHA_SKIP_PARAM(
            "captcha.skip.param", PwmSettingSyntax.STRING, PwmSettingCategory.CAPTCHA ),
    CAPTCHA_SKIP_COOKIE(
            "captcha.skip.cookie", PwmSettingSyntax.STRING, PwmSettingCategory.CAPTCHA ),
    CAPTCHA_INTRUDER_COUNT_TRIGGER(
            "captcha.intruderAttemptTrigger", PwmSettingSyntax.NUMERIC, PwmSettingCategory.CAPTCHA ),
    CAPTCHA_RECAPTCHA_MODE(
            "captcha.recaptcha.mode", PwmSettingSyntax.SELECT, PwmSettingCategory.CAPTCHA ),

    // intruder detection
    INTRUDER_ENABLE(
            "intruder.enable", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.INTRUDER_SETTINGS ),
    INTRUDER_STORAGE_METHOD(
            "intruder.storageMethod", PwmSettingSyntax.SELECT, PwmSettingCategory.INTRUDER_SYSTEM_SETTINGS ),
    SECURITY_SIMULATE_LDAP_BAD_PASSWORD(
            "security.ldap.simulateBadPassword", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.INTRUDER_SETTINGS ),

    INTRUDER_USER_RESET_TIME(
            "intruder.user.resetTime", PwmSettingSyntax.DURATION, PwmSettingCategory.INTRUDER_TIMEOUTS ),
    INTRUDER_USER_MAX_ATTEMPTS(
            "intruder.user.maxAttempts", PwmSettingSyntax.NUMERIC, PwmSettingCategory.INTRUDER_TIMEOUTS ),
    INTRUDER_USER_CHECK_TIME(
            "intruder.user.checkTime", PwmSettingSyntax.DURATION, PwmSettingCategory.INTRUDER_TIMEOUTS ),
    INTRUDER_ATTRIBUTE_RESET_TIME(
            "intruder.attribute.resetTime", PwmSettingSyntax.DURATION, PwmSettingCategory.INTRUDER_TIMEOUTS ),
    INTRUDER_ATTRIBUTE_MAX_ATTEMPTS(
            "intruder.attribute.maxAttempts", PwmSettingSyntax.NUMERIC, PwmSettingCategory.INTRUDER_TIMEOUTS ),
    INTRUDER_ATTRIBUTE_CHECK_TIME(
            "intruder.attribute.checkTime", PwmSettingSyntax.DURATION, PwmSettingCategory.INTRUDER_TIMEOUTS ),
    INTRUDER_TOKEN_DEST_RESET_TIME(
            "intruder.tokenDest.resetTime", PwmSettingSyntax.DURATION, PwmSettingCategory.INTRUDER_TIMEOUTS ),
    INTRUDER_TOKEN_DEST_MAX_ATTEMPTS(
            "intruder.tokenDest.maxAttempts", PwmSettingSyntax.NUMERIC, PwmSettingCategory.INTRUDER_TIMEOUTS ),
    INTRUDER_TOKEN_DEST_CHECK_TIME(
            "intruder.tokenDest.checkTime", PwmSettingSyntax.DURATION, PwmSettingCategory.INTRUDER_TIMEOUTS ),
    INTRUDER_ADDRESS_RESET_TIME(
            "intruder.address.resetTime", PwmSettingSyntax.DURATION, PwmSettingCategory.INTRUDER_TIMEOUTS ),
    INTRUDER_ADDRESS_MAX_ATTEMPTS(
            "intruder.address.maxAttempts", PwmSettingSyntax.NUMERIC, PwmSettingCategory.INTRUDER_TIMEOUTS ),
    INTRUDER_ADDRESS_CHECK_TIME(
            "intruder.address.checkTime", PwmSettingSyntax.DURATION, PwmSettingCategory.INTRUDER_TIMEOUTS ),
    INTRUDER_SESSION_MAX_ATTEMPTS(
            "intruder.session.maxAttempts", PwmSettingSyntax.NUMERIC, PwmSettingCategory.INTRUDER_TIMEOUTS ),

    // token settings
    TOKEN_STORAGEMETHOD(
            "token.storageMethod", PwmSettingSyntax.SELECT, PwmSettingCategory.TOKEN ),
    TOKEN_CHARACTERS(
            "token.characters", PwmSettingSyntax.STRING, PwmSettingCategory.TOKEN ),
    TOKEN_LENGTH(
            "token.length", PwmSettingSyntax.NUMERIC, PwmSettingCategory.TOKEN ),
    TOKEN_LIFETIME(
            "token.lifetime", PwmSettingSyntax.DURATION, PwmSettingCategory.TOKEN ),
    TOKEN_LDAP_ATTRIBUTE(
            "token.ldap.attribute", PwmSettingSyntax.STRING, PwmSettingCategory.TOKEN ),
    TOKEN_ENABLE_VALUE_MASKING(
            "token.valueMasking.enable", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.TOKEN ),

    // OTP
    OTP_PROFILE_LIST(
            "otp.profile.list", PwmSettingSyntax.PROFILE, PwmSettingCategory.INTERNAL_DOMAIN ),
    OTP_SETUP_USER_PERMISSION(
            "otp.secret.allowSetup.queryMatch", PwmSettingSyntax.USER_PERMISSION, PwmSettingCategory.OTP_PROFILE ),
    OTP_ALLOW_SETUP(
            "otp.enabled", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.OTP_PROFILE ),
    OTP_FORCE_SETUP(
            "otp.forceSetup", PwmSettingSyntax.SELECT, PwmSettingCategory.OTP_PROFILE ),
    OTP_SECRET_IDENTIFIER(
            "otp.secret.identifier", PwmSettingSyntax.STRING, PwmSettingCategory.OTP_PROFILE ),
    OTP_RECOVERY_CODES(
            "otp.secret.recoveryCodes", PwmSettingSyntax.NUMERIC, PwmSettingCategory.OTP_PROFILE ),

    OTP_SECRET_READ_PREFERENCE(
            "otp.secret.readPreference", PwmSettingSyntax.SELECT, PwmSettingCategory.OTP_SETTINGS ),
    OTP_SECRET_WRITE_PREFERENCE(
            "otp.secret.writePreference", PwmSettingSyntax.SELECT, PwmSettingCategory.OTP_SETTINGS ),
    OTP_SECRET_STORAGEFORMAT(
            "otp.secret.storageFormat", PwmSettingSyntax.SELECT, PwmSettingCategory.OTP_SETTINGS ),
    OTP_SECRET_ENCRYPT(
            "otp.secret.encrypt", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.OTP_SETTINGS ),

    // logger settings
    EVENTS_JAVA_STDOUT_LEVEL(
            "events.java.stdoutLevel", PwmSettingSyntax.SELECT, PwmSettingCategory.LOGGING ),
    EVENTS_LOCALDB_LOG_LEVEL(
            "events.pwmDB.logLevel", PwmSettingSyntax.SELECT, PwmSettingCategory.LOGGING ),
    EVENTS_FILE_LEVEL(
            "events.fileAppender.level", PwmSettingSyntax.SELECT, PwmSettingCategory.LOGGING ),
    EVENTS_PWMDB_MAX_EVENTS(
            "events.pwmDB.maxEvents", PwmSettingSyntax.NUMERIC, PwmSettingCategory.LOGGING ),
    EVENTS_PWMDB_MAX_AGE(
            "events.pwmDB.maxAge", PwmSettingSyntax.DURATION, PwmSettingCategory.LOGGING ),
    EVENTS_ALERT_DAILY_SUMMARY(
            "events.alert.dailySummary.enable", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.LOGGING ),
    EVENTS_JAVA_LOG4JCONFIG_FILE(
            "events.java.log4jconfigFile", PwmSettingSyntax.STRING, PwmSettingCategory.LOGGING ),

    PASSWORD_STRENGTH_METER_TYPE(
            "password.strengthMeter.type", PwmSettingSyntax.SELECT, PwmSettingCategory.LOGGING ),



    // auditingsettings
    AUDIT_SYSTEM_EVENTS(
            "audit.system.eventList", PwmSettingSyntax.OPTIONLIST, PwmSettingCategory.AUDIT_CONFIG ),
    AUDIT_USER_EVENTS(
            "audit.user.eventList", PwmSettingSyntax.OPTIONLIST, PwmSettingCategory.AUDIT_CONFIG ),
    EVENTS_AUDIT_MAX_AGE(
            "events.audit.maxAge", PwmSettingSyntax.DURATION, PwmSettingCategory.AUDIT_CONFIG ),
    EVENTS_AUDIT_MAX_EVENTS(
            "events.audit.maxEvents", PwmSettingSyntax.NUMERIC, PwmSettingCategory.AUDIT_CONFIG ),

    EVENTS_USER_STORAGE_METHOD(
            "events.user.storageMethod", PwmSettingSyntax.SELECT, PwmSettingCategory.USER_HISTORY ),
    EVENTS_USER_EVENT_TYPES(
            "events.user.eventList", PwmSettingSyntax.OPTIONLIST, PwmSettingCategory.USER_HISTORY ),
    EVENTS_LDAP_MAX_EVENTS(
            "events.ldap.maxEvents", PwmSettingSyntax.NUMERIC, PwmSettingCategory.USER_HISTORY ),

    AUDIT_EMAIL_SYSTEM_TO(
            "email.adminAlert.toAddress", PwmSettingSyntax.STRING_ARRAY, PwmSettingCategory.AUDIT_FORWARD ),
    AUDIT_EMAIL_USER_TO(
            "audit.userEvent.toAddress", PwmSettingSyntax.STRING_ARRAY, PwmSettingCategory.AUDIT_FORWARD ),
    AUDIT_SYSLOG_SERVERS(
            "audit.syslog.servers", PwmSettingSyntax.STRING_ARRAY, PwmSettingCategory.AUDIT_FORWARD ),
    AUDIT_SYSLOG_CERTIFICATES(
            "audit.syslog.certificates", PwmSettingSyntax.X509CERT, PwmSettingCategory.AUDIT_FORWARD ),
    AUDIT_SYSLOG_OUTPUT_FORMAT(
            "audit.syslog.outputFormat", PwmSettingSyntax.SELECT, PwmSettingCategory.AUDIT_FORWARD ),

    // challenge settings
    CHALLENGE_ENABLE(
            "challenge.enable", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.CHALLENGE ),
    CHALLENGE_FORCE_SETUP(
            "challenge.forceSetup", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.CHALLENGE ),
    CHALLENGE_SHOW_CONFIRMATION(
            "challenge.showConfirmation", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.CHALLENGE ),
    CHALLENGE_CASE_INSENSITIVE(
            "challenge.caseInsensitive", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.CHALLENGE ),
    CHALLENGE_ALLOW_DUPLICATE_RESPONSES(
            "challenge.allowDuplicateResponses", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.CHALLENGE ),
    QUERY_MATCH_SETUP_RESPONSE(
            "challenge.allowSetup.queryMatch", PwmSettingSyntax.USER_PERMISSION, PwmSettingCategory.CHALLENGE ),
    QUERY_MATCH_CHECK_RESPONSES(
            "command.checkResponses.queryMatch", PwmSettingSyntax.USER_PERMISSION, PwmSettingCategory.CHALLENGE ),

    // challenge policy profile
    CHALLENGE_PROFILE_LIST(
            "challenge.profile.list", PwmSettingSyntax.PROFILE, PwmSettingCategory.INTERNAL_DOMAIN ),
    CHALLENGE_POLICY_QUERY_MATCH(
            "challenge.policy.queryMatch", PwmSettingSyntax.USER_PERMISSION, PwmSettingCategory.CHALLENGE_POLICY ),
    CHALLENGE_RANDOM_CHALLENGES(
            "challenge.randomChallenges", PwmSettingSyntax.CHALLENGE, PwmSettingCategory.CHALLENGE_POLICY ),
    CHALLENGE_REQUIRED_CHALLENGES(
            "challenge.requiredChallenges", PwmSettingSyntax.CHALLENGE, PwmSettingCategory.CHALLENGE_POLICY ),
    CHALLENGE_MIN_RANDOM_REQUIRED(
            "challenge.minRandomRequired", PwmSettingSyntax.NUMERIC, PwmSettingCategory.CHALLENGE_POLICY ),
    CHALLENGE_MIN_RANDOM_SETUP(
            "challenge.minRandomsSetup", PwmSettingSyntax.NUMERIC, PwmSettingCategory.CHALLENGE_POLICY ),
    CHALLENGE_HELPDESK_RANDOM_CHALLENGES(
            "challenge.helpdesk.randomChallenges", PwmSettingSyntax.CHALLENGE, PwmSettingCategory.CHALLENGE_POLICY ),
    CHALLENGE_HELPDESK_REQUIRED_CHALLENGES(
            "challenge.helpdesk.requiredChallenges", PwmSettingSyntax.CHALLENGE, PwmSettingCategory.CHALLENGE_POLICY ),
    CHALLENGE_HELPDESK_MIN_RANDOM_SETUP(
            "challenge.helpdesk.minRandomsSetup", PwmSettingSyntax.NUMERIC, PwmSettingCategory.CHALLENGE_POLICY ),


    // recovery settings
    FORGOTTEN_PASSWORD_ENABLE(
            "recovery.enable", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.RECOVERY_SETTINGS ),
    FORGOTTEN_PASSWORD_SEARCH_FORM(
            "recovery.form", PwmSettingSyntax.FORM, PwmSettingCategory.RECOVERY_SETTINGS ),
    FORGOTTEN_PASSWORD_SEARCH_FILTER(
            "recovery.searchFilter", PwmSettingSyntax.STRING, PwmSettingCategory.RECOVERY_SETTINGS ),
    FORGOTTEN_PASSWORD_READ_PREFERENCE(
            "recovery.response.readPreference", PwmSettingSyntax.SELECT, PwmSettingCategory.RECOVERY_SETTINGS ),
    FORGOTTEN_PASSWORD_WRITE_PREFERENCE(
            "recovery.response.writePreference", PwmSettingSyntax.SELECT, PwmSettingCategory.RECOVERY_SETTINGS ),
    CHALLENGE_STORAGE_HASHED(
            "response.hashMethod", PwmSettingSyntax.SELECT, PwmSettingCategory.RECOVERY_SETTINGS ),
    RECOVERY_BOGUS_USER_ENABLE(
            "recovery.bogus.user.enable", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.RECOVERY_SETTINGS ),

    // recovery profile
    RECOVERY_PROFILE_LIST(
            "recovery.profile.list", PwmSettingSyntax.PROFILE, PwmSettingCategory.INTERNAL_DOMAIN ),
    RECOVERY_PROFILE_QUERY_MATCH(
            "recovery.queryMatch", PwmSettingSyntax.USER_PERMISSION, PwmSettingCategory.RECOVERY_DEF ),
    RECOVERY_VERIFICATION_METHODS(
            "recovery.verificationMethods", PwmSettingSyntax.VERIFICATION_METHOD, PwmSettingCategory.RECOVERY_DEF ),
    RECOVERY_TOKEN_SEND_METHOD(
            "challenge.token.sendMethod", PwmSettingSyntax.SELECT, PwmSettingCategory.RECOVERY_DEF ),
    RECOVERY_ACTION(
            "recovery.action", PwmSettingSyntax.SELECT, PwmSettingCategory.RECOVERY_DEF ),
    RECOVERY_SENDNEWPW_METHOD(
            "recovery.sendNewPassword.sendMethod", PwmSettingSyntax.SELECT, PwmSettingCategory.RECOVERY_DEF ),
    RECOVERY_ATTRIBUTE_FORM(
            "challenge.requiredAttributes", PwmSettingSyntax.FORM, PwmSettingCategory.RECOVERY_DEF ),

    // recover options
    RECOVERY_ALLOW_UNLOCK(
            "challenge.allowUnlock", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.RECOVERY_OPTIONS ),
    RECOVERY_ALLOW_WHEN_LOCKED(
            "recovery.allowWhenLocked", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.RECOVERY_OPTIONS ),
    TOKEN_RESEND_ENABLE(
            "recovery.token.resend.enable", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.RECOVERY_OPTIONS ),
    RECOVERY_MINIMUM_PASSWORD_LIFETIME_OPTIONS(
            "recovery.minimumPasswordLifetimeOptions", PwmSettingSyntax.SELECT, PwmSettingCategory.RECOVERY_OPTIONS ),
    RECOVERY_POST_ACTIONS(
            "recovery.postActions", PwmSettingSyntax.ACTION, PwmSettingCategory.RECOVERY_OPTIONS ),
    RECOVERY_AGREEMENT_MESSAGE(
            "recovery.changeAgreement", PwmSettingSyntax.LOCALIZED_TEXT_AREA, PwmSettingCategory.RECOVERY_OPTIONS ),


    // recovery oauth
    RECOVERY_OAUTH_ID_LOGIN_URL(
            "recovery.oauth.idserver.loginUrl", PwmSettingSyntax.STRING, PwmSettingCategory.RECOVERY_OAUTH ),
    RECOVERY_OAUTH_ID_CODERESOLVE_URL(
            "recovery.oauth.idserver.codeResolveUrl", PwmSettingSyntax.STRING, PwmSettingCategory.RECOVERY_OAUTH ),
    RECOVERY_OAUTH_ID_ATTRIBUTES_URL(
            "recovery.oauth.idserver.attributesUrl", PwmSettingSyntax.STRING, PwmSettingCategory.RECOVERY_OAUTH ),
    RECOVERY_OAUTH_ID_CERTIFICATE(
            "recovery.oauth.idserver.serverCerts", PwmSettingSyntax.X509CERT, PwmSettingCategory.RECOVERY_OAUTH ),
    RECOVERY_OAUTH_ID_CLIENTNAME(
            "recovery.oauth.idserver.clientName", PwmSettingSyntax.STRING, PwmSettingCategory.RECOVERY_OAUTH ),
    RECOVERY_OAUTH_ID_SECRET(
            "recovery.oauth.idserver.secret", PwmSettingSyntax.PASSWORD, PwmSettingCategory.RECOVERY_OAUTH ),
    RECOVERY_OAUTH_ID_DN_ATTRIBUTE_NAME(
            "recovery.oauth.idserver.dnAttributeName", PwmSettingSyntax.STRING, PwmSettingCategory.RECOVERY_OAUTH ),
    RECOVERY_OAUTH_ID_USERNAME_SEND_VALUE(
            "recovery.oauth.idserver.usernameSendValue", PwmSettingSyntax.STRING, PwmSettingCategory.RECOVERY_OAUTH ),


    // forgotten username
    FORGOTTEN_USERNAME_ENABLE(
            "forgottenUsername.enable", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.FORGOTTEN_USERNAME ),
    FORGOTTEN_USERNAME_FORM(
            "forgottenUsername.form", PwmSettingSyntax.FORM, PwmSettingCategory.FORGOTTEN_USERNAME ),
    FORGOTTEN_USERNAME_SEARCH_FILTER(
            "forgottenUsername.searchFilter", PwmSettingSyntax.STRING, PwmSettingCategory.FORGOTTEN_USERNAME ),
    FORGOTTEN_USERNAME_MESSAGE(
            "forgottenUsername.message", PwmSettingSyntax.LOCALIZED_TEXT_AREA, PwmSettingCategory.FORGOTTEN_USERNAME ),
    FORGOTTEN_USERNAME_SEND_USERNAME_METHOD(
            "forgottenUsername.sendUsername.sendMethod", PwmSettingSyntax.SELECT, PwmSettingCategory.FORGOTTEN_USERNAME ),


    // new user settings
    NEWUSER_ENABLE(
            "newUser.enable", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.NEWUSER_SETTINGS ),

    NEWUSER_PROFILE_LIST(
            "newUser.profile.list", PwmSettingSyntax.PROFILE, PwmSettingCategory.INTERNAL_DOMAIN ),

    NEWUSER_FORM(
            "newUser.form", PwmSettingSyntax.FORM, PwmSettingCategory.NEWUSER_PROFILE ),
    NEWUSER_LDAP_PROFILE(
            "newUser.ldapProfile", PwmSettingSyntax.STRING, PwmSettingCategory.NEWUSER_PROFILE ),
    NEWUSER_CONTEXT(
            "newUser.createContext", PwmSettingSyntax.STRING, PwmSettingCategory.NEWUSER_PROFILE ),
    NEWUSER_AGREEMENT_MESSAGE(
            "display.newuser.agreement", PwmSettingSyntax.LOCALIZED_TEXT_AREA, PwmSettingCategory.NEWUSER_PROFILE ),
    NEWUSER_PROFILE_DISPLAY_NAME(
            "newUser.profile.displayName", PwmSettingSyntax.LOCALIZED_STRING, PwmSettingCategory.NEWUSER_PROFILE ),
    NEWUSER_PROFILE_DISPLAY_VISIBLE(
            "newUser.profile.visible", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.NEWUSER_PROFILE ),
    NEWUSER_WRITE_ATTRIBUTES(
            "newUser.writeAttributes", PwmSettingSyntax.ACTION, PwmSettingCategory.NEWUSER_PROFILE ),
    NEWUSER_DELETE_ON_FAIL(
            "newUser.deleteOnFail", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.NEWUSER_PROFILE ),
    NEWUSER_LOGOUT_AFTER_CREATION(
            "newUser.logoutAfterCreation", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.NEWUSER_PROFILE ),
    NEWUSER_USERNAME_DEFINITION(
            "newUser.username.definition", PwmSettingSyntax.STRING_ARRAY, PwmSettingCategory.NEWUSER_PROFILE ),
    NEWUSER_EMAIL_VERIFICATION(
            "newUser.email.verification", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.NEWUSER_PROFILE ),
    NEWUSER_SMS_VERIFICATION(
            "newUser.sms.verification", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.NEWUSER_PROFILE ),
    NEWUSER_EXTERNAL_VERIFICATION(
            "newUser.external.verification", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.NEWUSER_PROFILE ),
    NEWUSER_PASSWORD_POLICY_USER(
            "newUser.passwordPolicy.user", PwmSettingSyntax.STRING, PwmSettingCategory.NEWUSER_PROFILE ),
    NEWUSER_MINIMUM_WAIT_TIME(
            "newUser.minimumWaitTime", PwmSettingSyntax.DURATION, PwmSettingCategory.NEWUSER_PROFILE ),
    NEWUSER_REDIRECT_URL(
            "newUser.redirectUrl", PwmSettingSyntax.STRING, PwmSettingCategory.NEWUSER_PROFILE ),
    NEWUSER_PROMPT_FOR_PASSWORD(
            "newUser.promptForPassword", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.NEWUSER_PROFILE ),
    NEWUSER_TOKEN_LIFETIME_EMAIL(
            "newUser.token.lifetime", PwmSettingSyntax.DURATION, PwmSettingCategory.NEWUSER_PROFILE ),
    NEWUSER_TOKEN_LIFETIME_SMS(
            "newUser.token.lifetime.sms", PwmSettingSyntax.DURATION, PwmSettingCategory.NEWUSER_PROFILE ),


    // guest settings
    GUEST_ENABLE(
            "guest.enable", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.GUEST ),
    GUEST_CONTEXT(
            "guest.createContext", PwmSettingSyntax.STRING, PwmSettingCategory.GUEST ),
    GUEST_ADMIN_GROUP(
            "guest.adminGroup", PwmSettingSyntax.USER_PERMISSION, PwmSettingCategory.GUEST ),
    GUEST_FORM(
            "guest.form", PwmSettingSyntax.FORM, PwmSettingCategory.GUEST ),
    GUEST_UPDATE_FORM(
            "guest.update.form", PwmSettingSyntax.FORM, PwmSettingCategory.GUEST ),
    GUEST_WRITE_ATTRIBUTES(
            "guest.writeAttributes", PwmSettingSyntax.ACTION, PwmSettingCategory.GUEST ),
    GUEST_ADMIN_ATTRIBUTE(
            "guest.adminAttribute", PwmSettingSyntax.STRING, PwmSettingCategory.GUEST ),
    GUEST_EDIT_ORIG_ADMIN_ONLY(
            "guest.editOriginalAdminOnly", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.GUEST ),
    GUEST_MAX_VALID_DAYS(
            "guest.maxValidDays", PwmSettingSyntax.NUMERIC, PwmSettingCategory.GUEST ),
    GUEST_EXPIRATION_ATTRIBUTE(
            "guest.expirationAttribute", PwmSettingSyntax.STRING, PwmSettingCategory.GUEST ),

    // activation settings
    ACTIVATE_USER_ENABLE(
            "activateUser.enable", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.ACTIVATION_SETTINGS ),
    ACTIVATE_USER_FORM(
            "activateUser.form", PwmSettingSyntax.FORM, PwmSettingCategory.ACTIVATION_SETTINGS ),
    ACTIVATE_USER_SEARCH_FILTER(
            "activateUser.searchFilter", PwmSettingSyntax.STRING, PwmSettingCategory.ACTIVATION_SETTINGS ),

    ACTIVATE_USER_PROFILE_LIST(
            "activateUser.profile.list", PwmSettingSyntax.PROFILE, PwmSettingCategory.INTERNAL_DOMAIN ),

    ACTIVATE_USER_QUERY_MATCH(
            "activateUser.queryMatch", PwmSettingSyntax.USER_PERMISSION, PwmSettingCategory.ACTIVATION_PROFILE ),
    ACTIVATE_USER_UNLOCK(
            "activateUser.allowUnlock", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.ACTIVATION_PROFILE ),
    ACTIVATE_TOKEN_SEND_METHOD(
            "activateUser.token.sendMethod", PwmSettingSyntax.SELECT, PwmSettingCategory.ACTIVATION_PROFILE ),
    ACTIVATE_AGREEMENT_MESSAGE(
            "display.activateUser.agreement", PwmSettingSyntax.LOCALIZED_TEXT_AREA, PwmSettingCategory.ACTIVATION_PROFILE ),
    ACTIVATE_USER_PRE_WRITE_ATTRIBUTES(
            "activateUser.writePreAttributes", PwmSettingSyntax.ACTION, PwmSettingCategory.ACTIVATION_PROFILE ),
    ACTIVATE_USER_POST_WRITE_ATTRIBUTES(
            "activateUser.writePostAttributes", PwmSettingSyntax.ACTION, PwmSettingCategory.ACTIVATION_PROFILE ),

    // update profile
    UPDATE_PROFILE_ENABLE(
            "updateAttributes.enable", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.UPDATE_SETTINGS ),
    UPDATE_PROFILE__PROFILE_LIST(
            "updateAttributes.profile.list", PwmSettingSyntax.PROFILE, PwmSettingCategory.INTERNAL_DOMAIN ),
    UPDATE_PROFILE_QUERY_MATCH(
            "updateAttributes.queryMatch", PwmSettingSyntax.USER_PERMISSION, PwmSettingCategory.UPDATE_PROFILE ),
    UPDATE_PROFILE_WRITE_ATTRIBUTES(
            "updateAttributes.writeAttributes", PwmSettingSyntax.ACTION, PwmSettingCategory.UPDATE_PROFILE ),
    UPDATE_PROFILE_FORCE_SETUP(
            "updateAttributes.forceSetup", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.UPDATE_PROFILE ),
    UPDATE_PROFILE_AGREEMENT_MESSAGE(
            "display.updateAttributes.agreement", PwmSettingSyntax.LOCALIZED_TEXT_AREA, PwmSettingCategory.UPDATE_PROFILE ),
    UPDATE_PROFILE_FORM(
            "updateAttributes.form", PwmSettingSyntax.FORM, PwmSettingCategory.UPDATE_PROFILE ),
    UPDATE_PROFILE_SHOW_CONFIRMATION(
            "updateAttributes.showConfirmation", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.UPDATE_PROFILE ),
    UPDATE_PROFILE_EMAIL_VERIFICATION(
            "updateAttributes.email.verification", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.UPDATE_PROFILE ),
    UPDATE_PROFILE_SMS_VERIFICATION(
            "updateAttributes.sms.verification", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.UPDATE_PROFILE ),
    UPDATE_PROFILE_TOKEN_LIFETIME_EMAIL(
            "updateAttributes.token.lifetime", PwmSettingSyntax.DURATION, PwmSettingCategory.UPDATE_PROFILE ),
    UPDATE_PROFILE_TOKEN_LIFETIME_SMS(
            "updateAttributes.token.lifetime.sms", PwmSettingSyntax.DURATION, PwmSettingCategory.UPDATE_PROFILE ),


    UPDATE_PROFILE_CUSTOMLINKS(
            "updateAttributes.customLinks", PwmSettingSyntax.CUSTOMLINKS, PwmSettingCategory.UPDATE_PROFILE ),

    // shortcut settings
    SHORTCUT_ENABLE(
            "shortcut.enable", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.SHORTCUT ),
    SHORTCUT_ITEMS(
            "shortcut.items", PwmSettingSyntax.LOCALIZED_STRING_ARRAY, PwmSettingCategory.SHORTCUT ),
    SHORTCUT_HEADER_NAMES(
            "shortcut.httpHeaders", PwmSettingSyntax.STRING_ARRAY, PwmSettingCategory.SHORTCUT ),
    SHORTCUT_NEW_WINDOW(
            "shortcut.newWindow", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.SHORTCUT ),

    // peoplesearch settings
    PEOPLE_SEARCH_ENABLE(
            "peopleSearch.enable", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.PEOPLE_SEARCH_SETTINGS ),
    PEOPLE_SEARCH_ENABLE_PUBLIC(
            "peopleSearch.enablePublic", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.PEOPLE_SEARCH_SETTINGS ),
    PEOPLE_SEARCH_PUBLIC_PROFILE(
            "peopleSearch.public.profile", PwmSettingSyntax.STRING, PwmSettingCategory.PEOPLE_SEARCH_SETTINGS ),
    PEOPLESEARCH_PROFILE_LIST(
            "peopleSearch.profile.list", PwmSettingSyntax.PROFILE, PwmSettingCategory.INTERNAL_DOMAIN ),
    PEOPLE_SEARCH_QUERY_MATCH(
            "peopleSearch.queryMatch", PwmSettingSyntax.USER_PERMISSION, PwmSettingCategory.PEOPLE_SEARCH_PROFILE ),
    PEOPLE_SEARCH_SEARCH_FORM(
            "peopleSearch.search.form", PwmSettingSyntax.FORM, PwmSettingCategory.PEOPLE_SEARCH_PROFILE ),
    PEOPLE_SEARCH_RESULT_FORM(
            "peopleSearch.result.form", PwmSettingSyntax.FORM, PwmSettingCategory.PEOPLE_SEARCH_PROFILE ),
    PEOPLE_SEARCH_DETAIL_FORM(
            "peopleSearch.detail.form", PwmSettingSyntax.FORM, PwmSettingCategory.PEOPLE_SEARCH_PROFILE ),
    PEOPLE_SEARCH_RESULT_LIMIT(
            "peopleSearch.result.limit", PwmSettingSyntax.NUMERIC, PwmSettingCategory.PEOPLE_SEARCH_PROFILE ),
    PEOPLE_SEARCH_USE_PROXY(
            "peopleSearch.useProxy", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.PEOPLE_SEARCH_PROFILE ),
    PEOPLE_SEARCH_DISPLAY_NAMES_CARD_LABELS(
            "peopleSearch.displayName.cardLabels", PwmSettingSyntax.STRING_ARRAY, PwmSettingCategory.PEOPLE_SEARCH_PROFILE ),
    PEOPLE_SEARCH_MAX_CACHE_SECONDS(
            "peopleSearch.maxCacheSeconds", PwmSettingSyntax.DURATION, PwmSettingCategory.PEOPLE_SEARCH_PROFILE ),
    PEOPLE_SEARCH_ENABLE_PHOTO(
            "peopleSearch.enablePhoto", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.PEOPLE_SEARCH_PROFILE ),
    PEOPLE_SEARCH_PHOTO_QUERY_FILTER(
            "peopleSearch.photo.queryFilter", PwmSettingSyntax.USER_PERMISSION, PwmSettingCategory.PEOPLE_SEARCH_PROFILE ),
    PEOPLE_SEARCH_SEARCH_FILTER(
            "peopleSearch.searchFilter", PwmSettingSyntax.STRING, PwmSettingCategory.PEOPLE_SEARCH_PROFILE ),
    PEOPLE_SEARCH_SEARCH_BASE(
            "peopleSearch.searchBase", PwmSettingSyntax.STRING_ARRAY, PwmSettingCategory.PEOPLE_SEARCH_PROFILE ),
    PEOPLE_SEARCH_ENABLE_ORGCHART(
            "peopleSearch.enableOrgChart", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.PEOPLE_SEARCH_PROFILE ),
    PEOPLE_SEARCH_ENABLE_EXPORT(
            "peopleSearch.enableExport", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.PEOPLE_SEARCH_PROFILE ),
    PEOPLE_SEARCH_ENABLE_TEAM_MAILTO(
            "peopleSearch.enableTeamMailto", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.PEOPLE_SEARCH_PROFILE ),
    PEOPLE_SEARCH_ENABLE_PRINTING(
            "peopleSearch.enablePrinting", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.PEOPLE_SEARCH_PROFILE ),
    PEOPLE_SEARCH_IDLE_TIMEOUT_SECONDS(
            "peopleSearch.idleTimeout", PwmSettingSyntax.DURATION, PwmSettingCategory.PEOPLE_SEARCH_PROFILE ),
    PEOPLE_SEARCH_ENABLE_ADVANCED_SEARCH(
            "peopleSearch.advancedSearch.enable", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.PEOPLE_SEARCH_PROFILE ),



    // edirectory settings
    EDIRECTORY_STORE_NMAS_RESPONSES(
            "ldap.edirectory.storeNmasResponses", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.EDIR_SETTINGS ),
    EDIRECTORY_USE_NMAS_RESPONSES(
            "ldap.edirectory.useNmasResponses", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.EDIR_SETTINGS ),
    EDIRECTORY_READ_USER_PWD(
            "ldap.edirectory.readUserPwd", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.EDIR_SETTINGS ),

    EDIRECTORY_READ_CHALLENGE_SET(
            "ldap.edirectory.readChallengeSets", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.EDIR_CR_SETTINGS ),
    EDIRECTORY_CR_MIN_RANDOM_DURING_SETUP(
            "ldap.edirectory.cr.minRandomDuringSetup", PwmSettingSyntax.NUMERIC, PwmSettingCategory.EDIR_CR_SETTINGS ),
    EDIRECTORY_CR_APPLY_WORDLIST(
            "ldap.edirectory.cr.applyWordlist", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.EDIR_CR_SETTINGS ),
    EDIRECTORY_CR_MAX_QUESTION_CHARS_IN__ANSWER(
            "ldap.edirectory.cr.maxQuestionCharsInAnswer", PwmSettingSyntax.NUMERIC, PwmSettingCategory.EDIR_CR_SETTINGS ),


    // active directory
    AD_USE_PROXY_FOR_FORGOTTEN(
            "ldap.ad.proxyForgotten", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.ACTIVE_DIRECTORY ),
    AD_ALLOW_AUTH_REQUIRE_NEW_PWD(
            "ldap.ad.allowAuth.requireNewPassword", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.ACTIVE_DIRECTORY ),
    AD_ALLOW_AUTH_EXPIRED(
            "ldap.ad.allowAuth.expired", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.ACTIVE_DIRECTORY ),
    AD_ENFORCE_PW_HISTORY_ON_SET(
            "ldap.ad.enforcePwHistoryOnSet", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.ACTIVE_DIRECTORY ),

    // active directory
    ORACLE_DS_ENABLE_MANIP_ALLOWCHANGETIME(
            "ldap.oracleDS.enable.manipAllowChangeTime", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.ORACLE_DS ),
    ORACLE_DS_ALLOW_AUTH_REQUIRE_NEW_PWD(
            "ldap.oracleDS.allowAuth.requireNewPassword", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.ORACLE_DS ),


    // helpdesk profile
    HELPDESK_ENABLE(
            "helpdesk.enable", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.HELPDESK_SETTINGS ),
    HELPDESK_PROFILE_LIST(
            "helpdesk.profile.list", PwmSettingSyntax.PROFILE, PwmSettingCategory.INTERNAL_DOMAIN ),
    HELPDESK_PROFILE_QUERY_MATCH(
            "helpdesk.queryMatch", PwmSettingSyntax.USER_PERMISSION, PwmSettingCategory.HELPDESK_BASE ),
    HELPDESK_SEARCH_FORM(
            "helpdesk.search.form", PwmSettingSyntax.FORM, PwmSettingCategory.HELPDESK_BASE ),
    HELPDESK_SEARCH_RESULT_FORM(
            "helpdesk.result.form", PwmSettingSyntax.FORM, PwmSettingCategory.HELPDESK_BASE ),
    HELPDESK_SEARCH_FILTERS(
            "helpdesk.search.filters", PwmSettingSyntax.USER_PERMISSION, PwmSettingCategory.HELPDESK_BASE ),
    HELPDESK_SEARCH_FILTER(
            "helpdesk.filter", PwmSettingSyntax.STRING, PwmSettingCategory.HELPDESK_BASE ),
    HELPDESK_SEARCH_BASE(
            "helpdesk.searchBase", PwmSettingSyntax.STRING_ARRAY, PwmSettingCategory.HELPDESK_BASE ),
    HELPDESK_DETAIL_FORM(
            "helpdesk.detail.form", PwmSettingSyntax.FORM, PwmSettingCategory.HELPDESK_BASE ),
    HELPDESK_RESULT_LIMIT(
            "helpdesk.result.limit", PwmSettingSyntax.NUMERIC, PwmSettingCategory.HELPDESK_BASE ),
    HELPDESK_SEND_PASSWORD(
            "helpdesk.sendPassword", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.HELPDESK_BASE ),
    HELPDESK_POST_SET_PASSWORD_WRITE_ATTRIBUTES(
            "helpdesk.setPassword.writeAttributes", PwmSettingSyntax.ACTION, PwmSettingCategory.HELPDESK_BASE ),
    HELPDESK_ACTIONS(
            "helpdesk.actions", PwmSettingSyntax.ACTION, PwmSettingCategory.HELPDESK_BASE ),
    HELPDESK_IDLE_TIMEOUT_SECONDS(
            "helpdesk.idleTimeout", PwmSettingSyntax.DURATION, PwmSettingCategory.HELPDESK_BASE ),
    HELPDESK_ENFORCE_PASSWORD_POLICY(
            "helpdesk.enforcePasswordPolicy", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.HELPDESK_BASE ),
    HELPDESK_CLEAR_RESPONSES(
            "helpdesk.clearResponses", PwmSettingSyntax.SELECT, PwmSettingCategory.HELPDESK_BASE ),
    HELPDESK_FORCE_PW_EXPIRATION(
            "helpdesk.forcePwExpiration", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.HELPDESK_BASE ),
    HELPDESK_USE_PROXY(
            "helpdesk.useProxy", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.HELPDESK_BASE ),
    HELPDESK_DISPLAY_NAMES_CARD_LABELS(
            "helpdesk.displayName.cardLabels", PwmSettingSyntax.STRING_ARRAY, PwmSettingCategory.HELPDESK_BASE ),
    HELPDESK_TOKEN_SEND_METHOD(
            "helpdesk.token.sendMethod", PwmSettingSyntax.SELECT, PwmSettingCategory.HELPDESK_BASE ),

    HELPDESK_VIEW_STATUS_VALUES(
            "helpdesk.viewStatusValues", PwmSettingSyntax.OPTIONLIST, PwmSettingCategory.HELPDESK_OPTIONS ),
    HELPDESK_SET_PASSWORD_MODE(
            "helpdesk.setPassword.mode", PwmSettingSyntax.SELECT, PwmSettingCategory.HELPDESK_OPTIONS ),
    HELPDESK_ENABLE_UNLOCK(
            "helpdesk.enableUnlock", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.HELPDESK_OPTIONS ),
    HELPDESK_CLEAR_RESPONSES_BUTTON(
            "helpdesk.clearResponses.button", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.HELPDESK_OPTIONS ),
    HELPDESK_CLEAR_OTP_BUTTON(
            "helpdesk.clearOtp.button", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.HELPDESK_OPTIONS ),
    HELPDESK_DELETE_USER_BUTTON(
            "helpdesk.deleteUser.button", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.HELPDESK_OPTIONS ),
    HELPDESK_PASSWORD_MASKVALUE(
            "helpdesk.setPassword.maskValue", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.HELPDESK_OPTIONS ),
    HELPDESK_ENABLE_PHOTOS(
            "helpdesk.enablePhotos", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.HELPDESK_OPTIONS ),
    HELPDESK_ENABLE_ADVANCED_SEARCH(
            "helpdesk.advancedSearch.enable", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.HELPDESK_OPTIONS ),


    HELPDESK_VERIFICATION_METHODS(
            "helpdesk.verificationMethods", PwmSettingSyntax.VERIFICATION_METHOD, PwmSettingCategory.HELPDESK_VERIFICATION ),
    HELPDESK_VERIFICATION_FORM(
            "helpdesk.verification.form", PwmSettingSyntax.FORM, PwmSettingCategory.HELPDESK_VERIFICATION ),


    // Database
    DATABASE_JDBC_DRIVER(
            "db.jdbc.driver", PwmSettingSyntax.FILE, PwmSettingCategory.DATABASE_SETTINGS ),
    DATABASE_CLASS(
            "db.classname", PwmSettingSyntax.STRING, PwmSettingCategory.DATABASE_SETTINGS ),
    DATABASE_URL(
            "db.url", PwmSettingSyntax.STRING, PwmSettingCategory.DATABASE_SETTINGS ),
    DATABASE_USERNAME(
            "db.username", PwmSettingSyntax.STRING, PwmSettingCategory.DATABASE_SETTINGS ),
    DATABASE_PASSWORD(
            "db.password", PwmSettingSyntax.PASSWORD, PwmSettingCategory.DATABASE_SETTINGS ),
    DB_VENDOR_TEMPLATE(
            "db.vendor.template", PwmSettingSyntax.SELECT, PwmSettingCategory.DATABASE_SETTINGS ),

    // Database advanced
    DATABASE_COLUMN_TYPE_KEY(
            "db.columnType.key", PwmSettingSyntax.STRING, PwmSettingCategory.DATABASE_ADV ),
    DATABASE_COLUMN_TYPE_VALUE(
            "db.columnType.value", PwmSettingSyntax.STRING, PwmSettingCategory.DATABASE_ADV ),
    DATABASE_DEBUG_TRACE(
            "db.debugTrace.enable", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.DATABASE_ADV ),

    // pw expiry notice
    PW_EXPY_NOTIFY_ENABLE(
            "pwNotify.enable", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.PW_EXP_NOTIFY ),
    PW_EXPY_NOTIFY_STORAGE_MODE(
            "pwNotify.storageMode", PwmSettingSyntax.SELECT, PwmSettingCategory.PW_EXP_NOTIFY ),

    PW_EXPY_NOTIFY_PERMISSION(
            "pwNotify.queryString", PwmSettingSyntax.USER_PERMISSION, PwmSettingCategory.PW_EXP_NOTIFY ),
    PW_EXPY_NOTIFY_INTERVAL(
            "pwNotify.intervals", PwmSettingSyntax.STRING_ARRAY, PwmSettingCategory.PW_EXP_NOTIFY ),
    PW_EXPY_NOTIFY_JOB_OFFSET(
            "pwNotify.job.offSet", PwmSettingSyntax.DURATION, PwmSettingCategory.PW_EXP_NOTIFY ),


    // reporting
    REPORTING_ENABLE_DAILY_JOB(
            "reporting.enable", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.REPORTING ),
    REPORTING_JOB_TIME_OFFSET(
            "reporting.job.timeOffset", PwmSettingSyntax.DURATION, PwmSettingCategory.REPORTING ),
    REPORTING_USER_MATCH(
            "reporting.ldap.userMatch", PwmSettingSyntax.USER_PERMISSION, PwmSettingCategory.UI_FEATURES ),
    REPORTING_MAX_QUERY_SIZE(
            "reporting.ldap.maxQuerySize", PwmSettingSyntax.NUMERIC, PwmSettingCategory.REPORTING ),
    REPORTING_JOB_INTENSITY(
            "reporting.job.intensity", PwmSettingSyntax.SELECT, PwmSettingCategory.REPORTING ),
    REPORTING_SUMMARY_DAY_VALUES(
            "reporting.summary.dayValues", PwmSettingSyntax.STRING_ARRAY, PwmSettingCategory.REPORTING ),

    // OAuth
    OAUTH_ID_LOGIN_URL(
            "oauth.idserver.loginUrl", PwmSettingSyntax.STRING, PwmSettingCategory.OAUTH ),
    OAUTH_ID_SCOPE(
            "oauth.idserver.scope", PwmSettingSyntax.STRING, PwmSettingCategory.OAUTH ),
    OAUTH_ID_CODERESOLVE_URL(
            "oauth.idserver.codeResolveUrl", PwmSettingSyntax.STRING, PwmSettingCategory.OAUTH ),
    OAUTH_ID_ATTRIBUTES_URL(
            "oauth.idserver.attributesUrl", PwmSettingSyntax.STRING, PwmSettingCategory.OAUTH ),
    OAUTH_ID_CERTIFICATE(
            "oauth.idserver.serverCerts", PwmSettingSyntax.X509CERT, PwmSettingCategory.OAUTH ),
    OAUTH_ID_CLIENTNAME(
            "oauth.idserver.clientName", PwmSettingSyntax.STRING, PwmSettingCategory.OAUTH ),
    OAUTH_ID_SECRET(
            "oauth.idserver.secret", PwmSettingSyntax.PASSWORD, PwmSettingCategory.OAUTH ),
    OAUTH_ID_DN_ATTRIBUTE_NAME(
            "oauth.idserver.dnAttributeName", PwmSettingSyntax.STRING, PwmSettingCategory.OAUTH ),

    // CAS SSO
    CAS_CLEAR_PASS_URL(
            "cas.clearPassUrl", PwmSettingSyntax.STRING, PwmSettingCategory.CAS_SSO ),
    CAS_CLEARPASS_KEY(
            "cas.clearPass.key", PwmSettingSyntax.FILE, PwmSettingCategory.CAS_SSO ),
    CAS_CLEARPASS_ALGORITHM(
            "cas.clearPass.alg", PwmSettingSyntax.STRING, PwmSettingCategory.CAS_SSO ),
    // http sso
    SSO_AUTH_HEADER_NAME(
            "security.sso.authHeaderName", PwmSettingSyntax.STRING, PwmSettingCategory.HTTP_SSO ),

    // basic auth sso
    BASIC_AUTH_ENABLED(
            "basicAuth.enable", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.BASIC_SSO ),
    BASIC_AUTH_FORCE(
            "forceBasicAuth", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.BASIC_SSO ),

    // administration
    QUERY_MATCH_PWM_ADMIN(
            "pwmAdmin.queryMatch", PwmSettingSyntax.USER_PERMISSION, PwmSettingCategory.ADMINISTRATION ),
    ADMIN_ALLOW_SKIP_FORCED_ACTIVITIES(
            "pwmAdmin.allowSkipForcedActivities", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.ADMINISTRATION ),


    ENABLE_EXTERNAL_WEBSERVICES(
            "external.webservices.enable", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.REST_SERVER ),
    WEBSERVICES_PUBLIC_ENABLE(
            "webservices.public.enable", PwmSettingSyntax.OPTIONLIST, PwmSettingCategory.REST_SERVER ),
    WEBSERVICES_EXTERNAL_SECRET(
            "webservices.external.secrets", PwmSettingSyntax.NAMED_SECRET, PwmSettingCategory.REST_SERVER ),
    WEBSERVICES_QUERY_MATCH(
            "webservices.queryMatch", PwmSettingSyntax.USER_PERMISSION, PwmSettingCategory.REST_SERVER ),
    WEBSERVICES_THIRDPARTY_QUERY_MATCH(
            "webservices.thirdParty.queryMatch", PwmSettingSyntax.USER_PERMISSION, PwmSettingCategory.REST_SERVER ),
    ENABLE_WEBSERVICES_READANSWERS(
            "webservices.enableReadAnswers", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.REST_SERVER ),

    EXTERNAL_MACROS_DEST_TOKEN_URLS(
            "external.destToken.urls", PwmSettingSyntax.STRING, PwmSettingCategory.REST_CLIENT ),
    EXTERNAL_PWCHECK_REST_URLS(
            "external.pwcheck.urls", PwmSettingSyntax.STRING, PwmSettingCategory.REST_CLIENT ),
    EXTERNAL_MACROS_REST_URLS(
            "external.macros.urls", PwmSettingSyntax.STRING_ARRAY, PwmSettingCategory.REST_CLIENT ),
    EXTERNAL_MACROS_REMOTE_RESPONSES_URL(
            "external.remoteResponses.url", PwmSettingSyntax.STRING, PwmSettingCategory.REST_CLIENT ),
    EXTERNAL_REMOTE_DATA_URL(
            "external.remoteData.url", PwmSettingSyntax.REMOTE_WEB_SERVICE, PwmSettingCategory.REST_CLIENT ),


    // http client
    HTTP_PROXY_URL(
            "http.proxy.url", PwmSettingSyntax.STRING, PwmSettingCategory.HTTP_CLIENT ),
    HTTP_PROXY_EXCEPTIONS(
            "http.proxy.exceptions", PwmSettingSyntax.STRING_ARRAY, PwmSettingCategory.HTTP_CLIENT ),

    // https server
    HTTPS_CERT(
            "https.server.cert", PwmSettingSyntax.PRIVATE_KEY, PwmSettingCategory.HTTPS_SERVER ),
    HTTPS_PROTOCOLS(
            "https.server.tls.protocols", PwmSettingSyntax.OPTIONLIST, PwmSettingCategory.HTTPS_SERVER ),
    HTTPS_CIPHERS(
            "https.server.tls.ciphers", PwmSettingSyntax.STRING, PwmSettingCategory.HTTPS_SERVER ),


    // deprecated.

    // deprecated 2019-06-01
    PUBLIC_HEALTH_STATS_WEBSERVICES(
            "webservices.healthStats.makePublic", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.REST_SERVER ),

    // deprecated 2019-01-20
    PEOPLE_SEARCH_DISPLAY_NAME(
            "peopleSearch.displayName.user", PwmSettingSyntax.STRING, PwmSettingCategory.PEOPLE_SEARCH_PROFILE ),

    // deprecated 2019-01-20
    HELPDESK_DETAIL_DISPLAY_NAME(
            "helpdesk.displayName", PwmSettingSyntax.STRING, PwmSettingCategory.HELPDESK_BASE ),

    // deprecated 2018-12-05
    REPORTING_SEARCH_FILTER(
            "reporting.ldap.searchFilter", PwmSettingSyntax.STRING, PwmSettingCategory.REPORTING ),

    // deprecated 2018-10-15
    PEOPLE_SEARCH_SEARCH_ATTRIBUTES(
            "peopleSearch.searchAttributes", PwmSettingSyntax.STRING_ARRAY, PwmSettingCategory.PEOPLE_SEARCH ),

    // deprecated 2018-02-27
    RECOVERY_ENFORCE_MINIMUM_PASSWORD_LIFETIME(
            "challenge.enforceMinimumPasswordLifetime", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.RECOVERY_OPTIONS ),

    UPDATE_PROFILE_CHECK_QUERY_MATCH(
            "updateAttributes.check.queryMatch", PwmSettingSyntax.USER_PERMISSION, PwmSettingCategory.UPDATE_PROFILE ),
    PASSWORD_POLICY_AD_COMPLEXITY(
            "password.policy.ADComplexity", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.PASSWORD_POLICY ),
    CHALLENGE_REQUIRE_RESPONSES(
            "challenge.requireResponses", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.RECOVERY_SETTINGS ),
    FORGOTTEN_PASSWORD_REQUIRE_OTP(
            "recovery.require.otp", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.RECOVERY_SETTINGS ),
    HELPDESK_ENABLE_OTP_VERIFY(
            "helpdesk.otp.verify", PwmSettingSyntax.BOOLEAN, PwmSettingCategory.HELPDESK_BASE ),;


    private static final Map<String, PwmSetting> KEY_MAP = Arrays.stream( values() )
            .collect( Collectors.toUnmodifiableMap( PwmSetting::getKey, pwmSetting -> pwmSetting ) );

    private static final Comparator<PwmSetting> MENU_LOCATION_COMPARATOR = Comparator.comparing(
            pwmSetting -> pwmSetting.toMenuLocationDebug( null, PwmConstants.DEFAULT_LOCALE ),
            Comparator.nullsLast( Comparator.naturalOrder() ) );

    private static final List<PwmSetting> SORTED_VALUES = Arrays.stream( values() )
            .sorted( MENU_LOCATION_COMPARATOR )
            .collect( Collectors.toUnmodifiableList() );

    private final String key;
    private final PwmSettingSyntax syntax;
    private final PwmSettingCategory category;
    private final PwmSettingMetaDataReader pwmSettingMetaDataReader;

    PwmSetting(
            final String key,
            final PwmSettingSyntax syntax,
            final PwmSettingCategory category
    )
    {
        this.key = key;
        this.syntax = syntax;
        this.category = category;
        this.pwmSettingMetaDataReader = new PwmSettingMetaDataReader( this );
    }

    public static Comparator<PwmSetting> menuLocationComparator()
    {
        return MENU_LOCATION_COMPARATOR;
    }

    public String getKey( )
    {
        return key;
    }

    public boolean isConfidential( )
    {
        return PwmSettingSyntax.PASSWORD == this.getSyntax();
    }

    public PwmSettingCategory getCategory( )
    {
        return category;
    }

    public PwmSettingSyntax getSyntax( )
    {
        return syntax;
    }

    private List<TemplateSetReference<StoredValue>> getDefaultValue()
    {
        return pwmSettingMetaDataReader.getDefaultValue();
    }

    public StoredValue getDefaultValue( final PwmSettingTemplateSet templateSet )
    {
        final List<TemplateSetReference<StoredValue>> defaultValues = getDefaultValue();
        return TemplateSetReference.referenceForTempleSet( defaultValues, templateSet );
    }

    public Map<String, String> getDefaultValueDebugStrings( final Locale locale )
    {
        final Map<String, String> returnObj = new LinkedHashMap<>();
        for ( final TemplateSetReference<StoredValue> templateSetReference : this.getDefaultValue() )
        {
            returnObj.put(
                    StringUtil.join( templateSetReference.getSettingTemplates(), "," ),
                    ( templateSetReference.getReference() ).toDebugString( locale )
            );
        }
        return Map.copyOf( returnObj );
    }

    public Map<PwmSettingProperty, String> getProperties( )
    {
        return pwmSettingMetaDataReader.getProperties();
    }

    public Set<PwmSettingFlag> getFlags( )
    {
        return pwmSettingMetaDataReader.getFlags();
    }

    public Map<String, String> getOptions()
    {
        return pwmSettingMetaDataReader.getOptions();
    }

    public String getLabel( final Locale locale )
    {
        return pwmSettingMetaDataReader.getLabel( locale );
    }

    public String getDescription( final Locale locale )
    {
        return pwmSettingMetaDataReader.getDescription( locale );
    }

    public String getExample( final PwmSettingTemplateSet template )
    {
        return pwmSettingMetaDataReader.getExample( template );
    }

    public boolean isRequired( )
    {
        return pwmSettingMetaDataReader.isRequired();
    }

    public boolean isHidden( )
    {
        return pwmSettingMetaDataReader.isHidden();
    }

    public int getLevel( )
    {
        return pwmSettingMetaDataReader.getLevel();
    }

    public Pattern getRegExPattern( )
    {
        return pwmSettingMetaDataReader.getRegExPattern();
    }

    public static Optional<PwmSetting> forKey( final String key )
    {
        return Optional.ofNullable( KEY_MAP.get( key ) );
    }

    public String toMenuLocationDebug(
            final String profileID,
            final Locale locale
    )
    {
        return pwmSettingMetaDataReader.toMenuLocationDebug( profileID, locale );
    }

    public Collection<LDAPPermissionInfo> getLDAPPermissionInfo()
    {
        return pwmSettingMetaDataReader.getLDAPPermissionInfo();
    }

    public static List<PwmSetting> sortedValues()
    {
        return SORTED_VALUES;
    }

    @Value
    static class TemplateSetReference<T>
    {
        private final T reference;
        private final Set<PwmSettingTemplate> settingTemplates;

        static <T> T referenceForTempleSet(
                final List<TemplateSetReference<T>> templateSetReferences,
                final PwmSettingTemplateSet pwmSettingTemplateSet
        )
        {
            final PwmSettingTemplateSet effectiveTemplateSet = pwmSettingTemplateSet == null
                    ? PwmSettingTemplateSet.getDefault()
                    : pwmSettingTemplateSet;

            if ( templateSetReferences == null || templateSetReferences.isEmpty() )
            {
                throw new IllegalStateException( "templateSetReferences can not be null" );
            }

            if ( templateSetReferences.size() == 1 )
            {
                return templateSetReferences.iterator().next().getReference();
            }

            for ( int matchCountExamSize = templateSetReferences.size(); matchCountExamSize > 0; matchCountExamSize-- )
            {
                for ( final TemplateSetReference<T> templateSetReference : templateSetReferences )
                {
                    final Set<PwmSettingTemplate> temporarySet = CollectionUtil.copiedEnumSet( templateSetReference.getSettingTemplates(), PwmSettingTemplate.class );
                    temporarySet.retainAll( effectiveTemplateSet.getTemplates() );
                    final int matchCount = temporarySet.size();
                    if ( matchCount == matchCountExamSize )
                    {
                        return templateSetReference.getReference();
                    }
                }
            }

            return templateSetReferences.iterator().next().getReference();
        }
    }
}
