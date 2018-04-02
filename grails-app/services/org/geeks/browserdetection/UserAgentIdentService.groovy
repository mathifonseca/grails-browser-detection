package org.geeks.browserdetection

import eu.bitwalker.useragentutils.UserAgent
import eu.bitwalker.useragentutils.Browser
import eu.bitwalker.useragentutils.OperatingSystem
import eu.bitwalker.useragentutils.RenderingEngine
import eu.bitwalker.useragentutils.BrowserType
import eu.bitwalker.useragentutils.DeviceType

class UserAgentIdentService extends WebTierService {

	final static String CHROME = "chrome"
    final static String FIREFOX = "firefox"
    final static String SAFARI = "safari"
    final static String OTHER = "other"
    final static String MSIE = "msie"
    final static String UNKNOWN = "unknown"
    final static String BLACKBERRY = "blackberry"
    final static String SEAMONKEY = "seamonkey"
    final static String OPERA = "opera"

    final static int CLIENT_CHROME = 0
    final static int CLIENT_FIREFOX = 1
    final static int CLIENT_SAFARI = 2
    final static int CLIENT_OTHER = 3
    final static int CLIENT_MSIE = 4
    final static int CLIENT_UNKNOWN = 5
    final static int CLIENT_BLACKBERRY = 6
    final static int CLIENT_SEAMONKEY = 7
    final static int CLIENT_OPERA = 8

	final static String AGENT_INFO_TOKEN = "${this.name}_agentInfo"

	final static def MOBILE_BROWSERS = [
            OperatingSystem.iOS4_IPHONE,
            OperatingSystem.iOS5_IPHONE,
            OperatingSystem.MAC_OS_X_IPAD,
			OperatingSystem.MAC_OS_X_IPHONE,
            OperatingSystem.MAC_OS_X_IPOD,
            OperatingSystem.BADA,
            OperatingSystem.PSP
    ]

	final static def MOBILE_BROWSER_GROUPS = [
            OperatingSystem.ANDROID,
            OperatingSystem.BLACKBERRY,
            OperatingSystem.KINDLE,
            OperatingSystem.SYMBIAN
    ]

	boolean transactional = false

	/**
	 * Returns user-agent header value from thread-bound RequestContextHolder
	 */
	String getUserAgentString() {
		getUserAgentString(getRequest())
	}

	/**
	 * Returns user-agent header value from the passed request
	 */
	String getUserAgentString(def request) {
		request.getHeader("user-agent")
	}

	def getUserAgent() {

		def userAgentString = getUserAgentString()
		def userAgent = request.session.getAttribute(AGENT_INFO_TOKEN)

		// returns cached instance
		if (userAgent != null && userAgent.userAgentString == userAgentString) {
			return userAgent
		}

		if (userAgent != null && userAgent.userAgentString != userAgent) {
			log.warn "User agent string has changed in a single session!"
			log.warn "Previous User Agent: ${userAgent.userAgentString}"
			log.warn "New User Agent: ${userAgentString}"
			log.warn "Discarding existing agent info and creating new..."
		} else {
			log.debug "User agent info does not exist in session scope, creating..."
		}

		// fallback for users without user-agent header
		if(userAgentString == null){
			log.warn "User agent header is not set"

			userAgentString = ""
		}

		userAgent = parseUserAgent(userAgentString)

		getRequest().session.setAttribute(AGENT_INFO_TOKEN, userAgent)
		return userAgent
	}

	def parseUserAgent(String userAgentString){
		UserAgent.parseUserAgentString(userAgentString)
	}

	boolean isChrome(ComparisonType comparisonType = null, String version = null) {
		isBrowser(Browser.CHROME, comparisonType, version)
	}

	boolean isFirefox(ComparisonType comparisonType = null, String version = null) {
		isBrowser(Browser.FIREFOX, comparisonType, version)
	}

	boolean isMsie(ComparisonType comparisonType = null, String version = null) {
		// why people use it?
		isBrowser(Browser.IE, comparisonType, version)
	}

	boolean isSafari(ComparisonType comparisonType = null, String version = null) {
		isBrowser(Browser.SAFARI, comparisonType, version)
	}

	boolean isOpera(ComparisonType comparisonType = null, String version = null) {
		isBrowser(Browser.OPERA, comparisonType, version)
	}

	/**
	 * Returns true if browser is unknown
	 */
	boolean isOther() {
		isBrowser(Browser.UNKNOWN)
	}

	private boolean isBrowser(Browser browserForChecking, ComparisonType comparisonType = null,
	                          String version = null){
		def userAgent = getUserAgent()
		def browser = userAgent.browser

		// browser checking
		if(!(browser.group == browserForChecking || browser == browserForChecking)){
			return false
		}

		// version checking
		if(version){
			if(!comparisonType){
				throw new IllegalArgumentException("comparisonType should be specified")
			}

            if (userAgent.browserVersion?.version) {

                if(comparisonType == ComparisonType.EQUAL){
                    return VersionHelper.equals(userAgent.browserVersion?.version, version)
                }

                def compRes = VersionHelper.compare(userAgent.browserVersion?.version, version)

                if(compRes == 1 && comparisonType == ComparisonType.GREATER){
                    return true
                }

                if(compRes == -1 && comparisonType == ComparisonType.LOWER){
                    return true
                }

            }

            return false
		}

		true
	}

	private boolean isOs(OperatingSystem osForChecking){
		def os = getUserAgent().operatingSystem

		os.group == osForChecking || os == osForChecking
	}

	boolean isiPhone() {
		def os = getUserAgent().operatingSystem

		os in [ OperatingSystem.MAC_OS_X_IPHONE,
				OperatingSystem.iOS4_IPHONE,
				OperatingSystem.iOS5_IPHONE,
				OperatingSystem.iOS6_IPHONE,
				OperatingSystem.iOS7_IPHONE,
				OperatingSystem.iOS8_IPHONE,
				OperatingSystem.iOS8_1_IPHONE,
				OperatingSystem.iOS8_2_IPHONE,
				OperatingSystem.iOS8_3_IPHONE,
				OperatingSystem.iOS8_4_IPHONE,
				OperatingSystem.iOS9_IPHONE ]
	}

	boolean isiPad() {
		def os = getUserAgent().operatingSystem
		os in [ OperatingSystem.MAC_OS_X_IPAD,
				OperatingSystem.iOS6_IPAD,
				OperatingSystem.iOS7_IPAD,
				OperatingSystem.iOS8_IPAD,
				OperatingSystem.iOS8_1_IPAD,
				OperatingSystem.iOS8_2_IPAD,
				OperatingSystem.iOS8_3_IPAD,
				OperatingSystem.iOS8_4_IPAD,
				OperatingSystem.iOS9_IPAD ]
	}

	boolean isiOsDevice() {
		isOs(OperatingSystem.IOS)
	}

	boolean isAndroid() {
		isOs(OperatingSystem.ANDROID)
	}

	boolean isAndroidTablet() {
		isOs(OperatingSystem.ANDROID) && getUserAgent().operatingSystem.deviceType == DeviceType.TABLET
	}

	boolean isTablet() {
		getUserAgent().operatingSystem.deviceType == DeviceType.TABLET
	}

	boolean isPalm() {
		isOs(OperatingSystem.PALM)
	}

	boolean isLinux(){
		isOs(OperatingSystem.LINUX)
	}

	boolean isWindows(){
		isOs(OperatingSystem.WINDOWS)
	}

	boolean isOSX(){
		isOs(OperatingSystem.MAC_OS_X)
	}

	boolean isWebkit() {
		getUserAgent().browser.renderingEngine == RenderingEngine.WEBKIT
	}

	boolean isWindowsMobile() {
		def os = getUserAgent().operatingSystem
		os in [ OperatingSystem.WINDOWS_MOBILE,
                OperatingSystem.WINDOWS_MOBILE7,
                OperatingSystem.WINDOWS_PHONE8,
                OperatingSystem.WINDOWS_PHONE8_1,
                OperatingSystem.WINDOWS_10_MOBILE
        ]
	}

	boolean isWindowsPhone() {
		isWindowsMobile()
	}

	boolean isBlackberry() {
		isOs(OperatingSystem.BLACKBERRY)
	}

	boolean isSeamonkey() {
		isBrowser(Browser.SEAMONKEY)
	}

	/**
	 * Returns true if client is a mobile phone or any Android device, iPhone, iPad, iPod, PSP, Blackberry, Bada device
	 */
	boolean isMobile() {
		def userAgent = getUserAgent()
		def os = userAgent.operatingSystem

		userAgent.browser.browserType == BrowserType.MOBILE_BROWSER || os in MOBILE_BROWSERS ||
				(os.group && os.group in MOBILE_BROWSER_GROUPS) || isiOsDevice()
	}

	boolean isRobot() {
		userAgent.browser.browserType == BrowserType.ROBOT
	}

	/**
	 * Returns the browser name.
	 */
	String getBrowser(){
		getUserAgent().browser.name
	}

	/**
	 * It is left for compatibility reasons. Use {@link #getBrowser() } instead.
	 */
	@Deprecated
	String getBrowserName(){
		switch (getBrowserType()) {
			case CLIENT_FIREFOX:
				return FIREFOX;
			case CLIENT_CHROME:
				return CHROME;
			case CLIENT_SAFARI:
				return SAFARI;
			case CLIENT_SEAMONKEY:
				return SEAMONKEY;
			case CLIENT_MSIE:
				return MSIE;
			case CLIENT_BLACKBERRY:
				return BLACKBERRY;
			case CLIENT_OPERA:
				return OPERA;
			case CLIENT_OTHER:
			case CLIENT_UNKNOWN:
			default:
				return OTHER;
		}
	}

	String getBrowserVersion() {
		getUserAgent()?.browserVersion?.version
	}

	String getOperatingSystem() {
		getUserAgent()?.operatingSystem?.name
	}

	@Deprecated
	String getPlatform() {
		getUserAgent().operatingSystem
	}

	/**
	 * Internet Explorer specific.
	 */
	@Deprecated
	String getSecurity() {
		throw new UnsupportedOperationException()
	}

	@Deprecated
	String getLanguage() {
		throw new UnsupportedOperationException()
	}

	@Deprecated
	int getBrowserType() {
		def browser = getUserAgent().browser
		browser = browser.group ? browser.group : browser

		switch (browser){
			case Browser.FIREFOX:
				return CLIENT_FIREFOX;
			case Browser.CHROME:
				return CLIENT_CHROME;
			case Browser.SAFARI:
				return CLIENT_SAFARI;
			case Browser.SEAMONKEY:
				return CLIENT_SEAMONKEY;
			case Browser.IE:
				return CLIENT_MSIE;
			case Browser.OPERA:
				return CLIENT_OPERA;
		}

		if(getUserAgent().operatingSystem == OperatingSystem.BLACKBERRY){
			return CLIENT_BLACKBERRY
		}

		return CLIENT_OTHER
	}

	/**
	 * It is left for compatibility reasons.
	 */
	@Deprecated
	def getUserAgentInfo() {
		def userAgent = getUserAgent()

		[
			browserType: getBrowserType(),
			browserVersion: userAgent.browserVersion?.version,
			operatingSystem: userAgent.operatingSystem?.name,
			platform: "",
			security: "",
			language: "",
			agentString: userAgent.userAgentString
		]
	}
}

public enum ComparisonType {
	LOWER,
	EQUAL,
	GREATER
}
