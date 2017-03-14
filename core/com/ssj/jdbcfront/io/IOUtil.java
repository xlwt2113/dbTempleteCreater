package com.ssj.jdbcfront.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import com.ssj.jdbcfront.util.FixedInputStream;

public class IOUtil {
	
	public static void main(String[] args) throws Exception{
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("C:/tmp.tmp"));
		byte byt = (byte) (Math.random()*256);
		StringBuffer string1 = new StringBuffer();
		StringBuffer string2 = new StringBuffer();
		String stringbuff="{'a':'°¡°¢ºÇß¹àÄëçï¹åH°®°«°¤°¥°­°©°¬°¦°§°ª°¯°£°¨´ôàÉæÈè¨êÓÞßíÁàÈïÍö°VÄË´ƒvƒŒƒùØÜ„’…¥ßÀ…Ù†‡Bàæ‡†ˆì‰a‰¹ÆæŠÖŠâ‹ÜÌÛ‘°‘¹”±”²•l•á™üšGš±œÜœâžGŸCŸs­a°}°Š²}³v´oµK½iËBÌ@ÖL×c×rÙŒÜtá{æXèPéuºÒêiêqëBì\ìaðgñLòIöJ÷oøÑÂ°´°²°µ°¶°³°¸°°°±°·³§¹ãâÖÞîáíï§èñÚÏðÆÛû÷öóƒ‡…\…{†H†††±ˆˆ¥ˆÝ‹F‹jŒå^¸É••›¡«q¯uºÐ±Q±V´UÁOÄWÇIÈCÈsÈ€ÉŽÑsÕYÖOØtØßVãQä@åBÇ¯éœêŽê›ë@ëˆì”íí™îOñüñKõcø‘ùgù“°º°¹°»Ñö…nŒì•n–‹áZálóa°À°¼°Á°Â°¾°Ã°½°¿°ÄÏùÞÖæÁâÚæñà»ÛêåÛñúòüéáöË÷¡÷éá®…†õàÞ‡Æ‡Ìˆ‰¥‰§ŠSŠW‹‹‹®CåŽS‘R’U’j“³“ý–À—`¹÷›|½½E²ÁŸÑ nª‡­H±l´x´“´ÂKÂOÆbÊTÎ‚Ò\Ö’Ö“ÝEàUçGéOëJòˆö—ø^ø€úqü',"+
			"b':'°Ñ°Ë°É°Ö°Î°Õ°Ï°Í°Å°Ç°Ó°Ô°È°Ð°Ê°Ì°Ò°ÆôÎÜØá±öÑîÙ÷ÉÝÃå±”²®…©†\†^ˆzˆ¢‰‰ÎŠBŠ‚Qy’i’pÞã–[èË–Â™ñÅÈžß ã«X°j°q³F¸Ÿ¼“ÁTÁjÃ_ÆžÝÉÍMÒ†ÔyØ^Ú•ÝRá—ášâZïTôƒõEõN÷„÷ˆü–°Ù°×°Ú°Ü°Ø°Ý°Û²®°ÞÞãßÂêþ†hŽß°Ç’…’“ÅÅ”[”¡–àÅÉªW¸q»“»Ÿ½]ÞµËbÒoÙ”ì‹÷¹ív°ë°ì°à°ã°è°á°æ°ß°å°é°â°ç°ê°ä°íñ­ÛàîÓô²Úæñ£K·ÖˆmˆÐŠ”Œê±òE“„”‘”Ê•L–D–®œ°­š¶t»O»{½OÃRÎZÎ†ÎŒÑ—ÒƒáÙÛAÞkÞl±æ±çÞnÞqâkã[é›ì‡îCô‘øX°ï°ô°ó°õ°÷°î°ñ°ö°ø°ð°ò°ùäºÝòK†çˆ ˆÈ‰Y‹˜LŽ°ŽÀŽÍÅíÏ’²’Ê“sÅÔ—” ¥«g³‰¶œ¼½‰¿R·ÄÅÍKÍ{ÎMó¦Örß™æ^íDòuóo°ü±§±¨±¥±£±©±¡±¦±¬°þ±ªÅÙ±¢°ý±¤°ú°û±«ÅÚÆÙöµæßìÒñÙð±õÀÝáÜƒ˜„ƒÙè„ô´ô‡E‡¥ˆçˆó‹~‹›Œ‡Œ—ŒšÞA•Þ–¢«’³h·‘¸²¾¾‹Ç˜Ê}ËÌ™ÍdÐˆÅÛÙöÑfÒJÙ…ãEètè˜é–ìdìsï’ï–ñhóbóŽõUøRødý_å²È`±»±±±¶±­±³±¯±¸±®±°±´±²±µ±º±·±¹±ÛñØã£ÝíðÇöÍßÂÚý÷¹ØÃÚéíÕ‚pÙÂ‚³‚Ë‚äƒF†\†h†Õˆ¢ÛýâöÊ‘v“d•K–{–È—G—f—“—”—À²¨ ´ ÍªN¬D¬i¯w° ¶F¹t¼LÆpÆ…ÆÐÝÉÈiÆÏËÍ“òãÒoÕRÕ|Ø°ÏÝKÝ…àfãmä^åCèEócùl±¾±¼±½±¿º»ï¼êÚÛÎÛÐÌå‚–†Ï‰úŠM’Ù“à—L—ñ›yœ`žÇŸø ÄªŠÁÏnÙSÝ™ßGåQèM±Ä±Á±Â±À±Å°ö±Ãê´àÔÈÙº°ø‚õßô†çˆ©ˆÈÜ¡‰lŠRÐÆ½Åê’²“sÅÔ°ñmŸÔ¬a¬e¯nµp½l¾X¿‡ÈEÛMßJåAçaéGéaìž±È±Ê±Õ±Ç±Ì±Ø±Ü±Æ±Ï±Û±Ë±É±Ú±Í±Ò±×±Ù±Î±Ð±Ó±Ö±Ý±Ñ±ÔÃØÃÚïõÞµÝ©ÜÅÝÉØ°ñÔî¯ÙÂæÔáùóÙóëó÷ô°ÜêôÅâØîéõÏßÁã¹êÚääå¨èµßÙ÷Âåöåþæ¾ØòÓØ·ð‚¿„ö±°…ñ†ž†ôˆfÛýˆã¸´‰ýŠ`ŠŒŠËæÇ‹ïŒÂš·ùŽÅŽÆâÏYŒ’PWÏ·÷Þã”À”è·þ–aèÁ–Š–©–Ä—a—À—ééÞéèšÈ›a²¨œ œü§Ÿ•ŸÎª‹ªŒ«®n®w®…¯H¯R¯w°n°zÆ³µ–·K¸“¹P¹t¹u¹vº`»z»¼„¼ž¾a¿oÀVç¢ÁTÁXÁ‘ÂÃYÃZÃ^Æ¢ÄMÄbÆƒÈ]ÉœÌYÂÇÍšÎ“Ð‹±»ÒKÒgÓvÔv×ØPØ„ÙCÙMÙSÚF·ÑÚPõËÛ~Û‹ÜKÜLß›àˆàŠâtãGåCæqç@èEöÍé[é\é]ésê\ÚéÚðì‹í@íSí{ïð{ñEð¥ñƒòóôxô“õIõmös÷”øpùSù›úzú‡ûGü„÷Ô±ß±ä±ã±é±à±ç±â±á±Þ±å±æ±èâíí¾ØÒãêíÜòùñÛöýóÖÜÐñ¹ÛÍçÂìÔ‰ä·âOÌÆ’\ÞÕ“O•c›Mž× ¤ªpª ®K¯V·Hð¡¹»e¼D¾Ž¾œÅXÅŒÈqËxÒŒÓS×ƒØPÙHÞgÞkÞlÞpÞqß„ß…ß›áŠæQérì™îYöböcøuú@÷Ô±í±ê±ë±ìè¼æ»ì©ì­÷§ñ¦ì®ïðñÑæôïÚ‚lƒGƒšØâ‰wæÎŽ¼Ò“¿˜Ë™~œWœýÆ¯ždìáŸÏ gªY·…ºgÃ ÄrÅA°úÊEË‘ÒFÕ•Ö€Ù™ål÷éçSèsïRï[ïjïkïlïnòŠóQóT÷Bû÷Ô±ð±ï±î±ñõ¿„e…ñ•Ö°Ç°Î°Æ“ÅÆ²–Â–Äªm°TÃØ·ÆƒÇa±ÎÌ‹ÍrÏhÒX÷Mü‚ý–±ö±ô±÷±ò±ó±õáÙë÷éëçÍ÷ÆÙÏéÄ÷ÞïÙçã·Ýƒ†”P—Ãš›šàšñäºžIžMžl¬ž­p³WÀ_ÄœÌžÏ™ÓŸØhÙeÙfÙšÚSß“è\ìEîlî Æµóxó‰óôW²¢²¡±ø±ù±û±ýÆÁ±ü±ú±þÞðéÄÙ÷ÚûK•ã‚v‚§‚ìÙûˆ—Œ}Æ½ŽÕŽðT’mÆ´’ò•\•mèÊ–Þ–â—€—Š™‰šê ]ìÞ¬V¯n°R°S±}·A·’¸p½l½Žç®ÆuÍsÕ@ÛMâãuä‰êvìhìí@íSïžðVõmðÚ²¦²¨²¥²´²©²®²µ²£°þ±¡²ª²¤²§²«²±²¯°ã°Ø²°²³²¬²­²²ÆÇ²·íçõËéÞÙñð¾õÛà£Þ¬ô¤îàâÄë¢·ð‚Nƒ`ƒk„ƒÄ¼†\‡h‡¥‰®Š‚ØÃŒXóŽ“°ÅÂö‘ÅÅÄ°Î’©“Üß¨±©·þ–Â—K˜_™q™Øš†ÆÃ›Âœ_œ”ŠÅËÆÙŸ¹±¬ ¦ Ý é þªt­“­”·¬°h°l°×°Ù°±C³j´B´‘µR¶z·q¸Ÿ¹º~²¾¼\¼žÀÃJÃ`Å‡åõÆtÆ…ÆžÆÐÈ•ÆÑÊNÊXÞµÞÁÌYÍoÐ“ÑBÑJÒTÒUÒqÔy×LØmõÀÅÜÜ@àRâ“ãKã\äcænè}éDðGðoñAñCñFñgñ•ò’ómópõEõN÷QöÑ÷ˆùPêþ²»²½²¹²¼²¿²¶²·²¾²¸±¤²º²ÀÆÒê³åÍêÎîßîÐõ³ß²ÑƒW„Ï…Ä…ùˆ¶ŠçŒ mŽïE‹’pÞÔ’Ã’Ñ“ä“ò–¿šhšiäßªŽ¶¹rº^Ç[Éž±¡ÑaÕcÛYÝ•ÞKà^âbâ˜¸½ê†÷¹ðJðXõ‹øGùLûQ',"+
			"c':'²Á²ðíåàê‚ð‡Í”c™U´~µg²Ìßn²Å²Ë²É²Ä²Æ²Ã²Â²È²Ç²Ì²Ê‚š‚Æ†’ˆÆŠéŒu‘å’A’ñ“H—¾Z¿nÀuØ”ÛP²Ï²Ð²ô²Î²Ò²Ñ²Í²Óæîè²åî÷õôÓ‚ðƒ……¢…£…¤†Ð‡A‡k‡Ô‹Û‹ìß‘K‘L‘M‘”‘â“·•üšˆœ\œ’Ó N |·_ºdËLÎ]ÐQÐTÖÛŠçDï{ïŠò‰öYöŸ÷“üo²Ø²Ö²×²Õ²ÔØ÷¨‚}‚áƒû…MÈ™âœæžPª¬šº[À˜ê°Å“ÉnÊiÏ@Ù‰è†úIû]²Ý²Ù²Ü²Û²ÚàÐô½ó©äî‚óåøæóý‘F‘¨“Ù•ùÔèÃHÆHÜ³É˜ÒGÒ_Ôìà“ç[èAòxü²á²à²ß²â²Þâü‚ÈƒÔ…‹‰x‹¨àýŽ¾ŽúÅ‘Š’‘”˜–ÅÕ¤œy®‚¸ž¹Z¹k¹‹ºu»ÇRÈYÈmÉƒÉâývØÖ²Îá¯ä¹…¢…£…¤ß—q›N³•·_¸’ºdÄ~Ôø²ã²äàáÉ®ÔöŒÓò™I¸}¿•çÕòš³€³’Kªeu²é²å²æ²è²î²í²ë²ì²ç²êÉ²²ïé«é¶ïïñÃãââÇéßæ±è¾ïÊàêš÷‚²„x†âÍÁˆ“ŠgŒð¿’K’Q’·’¼½Ý½Ó“c“ Ð±–Ë—^âª®›¶g¼pÃPÅaÅ‘ÆOÜÚÇNÝ±Ñ–ÓÔˆÔŒÛ‚âOã˜åšæ\èdîÎìxðlÔû²ñ²ð²î²òîÎðûò²Ù­ƒŠ„Ð²æ†¶‡Ð’K´ê²é åµ}ÜëÆÊOÏŠÐƒÓâOýbö·²ú²ø²ô²ó²û²ü²ù²÷²õµ¥²öêèæ¿ÝÛÚÆÙæâÜåîó¸åñïââãäýìøæöõðå¤P×ƒ]ƒdƒ{ƒ§ƒ·ÍÃ„i„}„•„­…gÀå†®†Î‡c‡Á‡ÏˆF‰‰Ê‹ÈæÓÕ¸ÝãäŽfŽÂŽÊ‘„‘Ï‘Ôµ§“˜“·“½“Û”v”â•C—{—˜^™Ùš´Õ´›º½¥œµIu¨žežž¬ŸžŸíª†®a®b³ƒ´v¶Uºo¾g¿CÀAÀWÀpÀsÕÍÃˆÆBÉ»ÊrÏMÏsÏ€ÐŸñÏÑgÒRÒbÒcÒ—ÕSÕ~Ö×€×‹×ÚßÛ…Þ{àšàžápã@äaäiçPèéKéˆêUí]îð’³¤³ª³£³¡³§³¢³¦³©²ý³¨³«³¥²þÉÑöðë©ÝÅã®æÏáäÛËãÑâêØöêÆÜÉæ½Ÿ‚tÌÈƒYƒ”ƒ¯ƒ¸…”‡L‡Ÿˆö‰jŒ¬ÉÐS•˜•³—–—ÇÌÊœCŸ…«`¬d¬„¬ ®D®^®˜Ã›ÄcÄqÈOÏ^ÑmÕkää–å_çLè éLéMé‹êOíoöK÷l÷•ü³¯³­³¬³³³±³²³´³°½Ë´Â³®â÷ìÌñéêËž£„¤„àßë‡ZŽlŽz€“¼˜©˜È™ùÌÎRýŸqŸ· Ÿ±|¸J»}½B¾K¾b¿U¿žç§ÉÜÁVÓeÔNÖaÖšÖßÚ}ÚˆÞCà}ânûžü{ü…³µ³·³¶³¸³¹³ß³ºÛåíº¼‚e‚®„ï…ã†q†Ã¶à‰ïŠbÕ¬åøØ“F“µ“Ý³â³ØŸEŸLŸc …²u³Œ³ÂsÇpÍ’ÔaÖÜ‡ÞŠîJ³Ã³Æ³½³¼³¾³¿³Á³Â³Ä³È³À³»é´ÞÓÚÈí×Úßå·ö³àÁØ÷è¡‚E‚áƒ¡‡¸¿°‰\Ìî‰m‰}‰öÁ±×’×“Z”•æÕí—F—£—²˜¹™ÂÉòÕ¿žcŸGŸ‹¯MÕî¯„¯’íñ²_³•´~·Q¾D¿bçÇëÀëÏÆÇ_ÇkÊcËlÏIÒrÔHÕ€ÖRÖnÖ×ÙoÙ•ÚfÚ’Ú™Û{Ü•Þápâ\åŒêJ´³êëúmû‰ýYýZ³Ó³É³ËÊ¢³Å³Æ³Ç³Ì³Ê³Ï³Í³Ñ³Ò³Î³È³ÐëóèßÛôîñàáîõõ¨êÉñÎèÇòÉØ©îª\Øö‚D‚t‚ ƒ\¾»Çº†ÜˆÁˆá‰SŠ¿ŒkwáÓŽñ»Õáç‘‘r‘~‘ÍÇÀ’¬’Þ“Œ“£“¬“Î“Õ³¨–b—–—¢—¼˜ŒéÌ˜û˜ü™f™ršé›„›“ä¥›Æ›ÕœQœË¯žjžs  ª«ž¬A¬b¬š®—¶¢±²…³·Q··œ¸V¹f½†¿BÃwÃ”Ç^ÍBÏ|ÓcÕ\ÚWÚXÌËÛk±ÄàJÛ«ÐÑä…æjçdçpèKêpìlîdð‰òGòrõ“üh³Ó³Ô³ß³Ù³Ø³á³Õ³à³Ý³Ü³Ö³â³Þ³Ú³Û³ã³×õØÛæÜÝÜ¯âÁæÊôùñÝë·ßêñ¡ò¿à´ð·í÷ó¤óø÷Îß³áÜó×àÍÙÑÌø‚s„„„È…h…q…µ…Õ…ä…æ…ê¶ßÐ¥Ï²†Ë‡V‡[‡i‡„ˆkˆ‰ŠLËýŠwŒÑI«¯Ópu‘J‘d‘y‘´’LÌ§’x²ð’„ÍÏ’’»ÌáÞõ“¤“¹”~–o–«èÜ˜»šIš^šlšnšýãû›nÖÎ›‚œFœ‰ÖÍœþkžÃŸUŸë ô®E¯b¯v¯€°Víô²lµoÀëÒÆ¸‡¹M¹x¹}»Œ¼Y½‚Á‹Â@ÂBÂ]ÃLëÕÃnÃqÃ’ÄSÜÎ²çÇKÀòÇ ÍNÍhÉßÎyÑDÑEÑlÔWÔ ÕBÕvÖsÖ–ÙPÚdÚmÚpÚ†Ú—ÛFÛLõ½ÛyÝBÞ‹ÞŒßWßgßoßtß†ãMãrã‰å~ëxë†ï†ï—ðSð„ÊÎñYòóPæêøTø|ùAù`ùù•ù—úuüJü[ýXýcÛ­³åÖØ³æ³ä³è³çÓ¿ÖÖô¾âçô©ï¥ã¿Üû‚£‚òÙ×†Á†üˆÃŒ™ƒ×‘o“_“›˜¶›_›ÒräüÖò ‚«–¯\ÖÑµr·N¾…ÁZÁˆ¼ëÎuÏxÐnÑ~ÛŒÛ Íªã|ê™³é³î³ô³ð³ó³í³ñ³ê³ï³ì³ë³òã°Ù±àüñ¬öÅE‚G‚¸ƒ‰…Á‡œÛÚæ¨‹B‹‹áŽÎJ‘ÀÅ¤’ôÞí“o”F–ƒ–„–ä—¹™„šŽäå b ¶ ß â®‡® °{±T±y²ƒºN»I¼—½[¾IÅWÇ“ËgÑnÔ—Öa×p×‡×‰Öß×žÛSÜPßcáOábáhá~âoÅ¥ëlô{õ\õöÖ³ö´¦³õ³ú³ý´¥³÷³þ´¡´¢Ðó³ü´£´¤³ù³ø³ûèúèÆÛ»âðç©Ø¡ãÀõé÷íòÜéË‚m‚âƒƒ¦„IÖú‡bˆÇ‹ƒŒçŽÐN‘A‘Ã’}“ª“¹”™”ßÄû—Æ˜Z˜™[™s™Ÿ™¬™»™úšbÍ¿Êçœäéžã Ë¬G¬`­lÁòµA×£µ—¸a¸eºX½IÐõÂ^ÂaÄ•ÆcÇˆÖøÉZÉeÉÊxË ÌŽÏ{ÐEñÒÓcÓ|ÔxÕ‘ÖTÚ°ÖîØXØaØŒÚnÛHÛUÛuÜXàsãIäzézërúRúžýiýsýƒåø´éšHš_´§ëúà¨àÜÞõõßšIÄDÄu´©´¬´«´®´¨´­´ªë°å×îËô­â¶çÝ‚÷ƒb„”‡ù‰@ã·Þò•ÄšNšöªk«[¬®Uº@ÄxÅxÇFÙiÛwõßÝŽâAïéúE´°´²´³´´´¯´±âë²Ö¨‚}‚ü„V„k„y„€„“‡l‡è´Ñí‘ê“œ–S™Hrw — ¡ §¯´}·™¸RÄ€ô©´ÐÊ[êJ´µ´¹´¶´¸´·×µé³é¢Úï‚…‡ùˆ§·“€–û¹ŠÄDÇ”à]åNæmîqôDôsý—´º´½´¿´À´¼´¾´»òíÝ»ðÈ‚¤ƒb‰@‹aÃ•I•«ëÔ–~˜J˜‡˜ê™šãç›Ìœ·œ÷_ Æ¬t²Q¹—¼ƒëÆÃaÃ‹ÄxÆXÈNÈoÉOÉ”ÙƒÛwÝbÝéúácåTêõžöjù‡ùœ´Á´ÂõÖà¨öºê¡´Ù…É‡ÇŠÅŠÆ‹C‹SŒF·’‘“óí½šf›íÄ×ìÌ´‡¹–´Ø¾Y¾b¿ž×ºÄJÜõÝýÚ}õÀÛTõâ³ùÝzÞuåÁßOáQÈ©äråYæ—èqïßýpýw´Î´Ë´Ê´É´È´Æ´Å´Ç´Ì´ÄËÅ´Ã´Í²î×ÈßÚðËìôôÙÕè‚½„p²Þ…‹Ë¾…è†ˆˆˆôŠœ‹ãáÏÕŽãŽú´ë–c–Ÿ–²²ñ–Ü–æ›×ÌÐžB«u«y®N°rµQôÒ½a¿WÃhÜëÆ˜ÆÜùÇ„ÈWËFËjòºÍyó£ÎˆÏ…Ô~ÙnÚaÚeôôÚÞeÞiÞoâ‘ï“ð@ódóqõJøyú\ú]ý€†ï´Ó´Ô´Ð´Ò´Ï´ÑçýèÈäÈè®æõÜÊÙÌ…²‡èŠæŒQ¾ÀÄòS•›‘F‘m•¾—Œ˜B˜º˜Ú˜â™ßY^|ƒœžšŸtŸÐ Q ­B²j³Ÿ´°ºb¾t¾‘¿k¿v¿‚ÀS×ÝÂ‡ÂŒÂ”Æ‰ÇˆÉÊ[ËqÏZÕpÖÙzÙ{çWò^ò‹´Õé¨ê£ëí×à×á×åœ«u´ØÝýÞ´Ë’Ç÷È¤Ú…Ý´Ö´×´Ø´Ù×äáÞâ§Ýýõ¾õ¡éãõíÇÒ…a‡mŠÅŠÆ‹{I‘–ÆÝ’Û¯|¯•°š¿U¿qç§ÃÊIÊPÓcÕKÇ÷ÚuÈ¤Ú‚Ú…ÛUÛcÛnÛqÜAåe´íî•û€û‚û„û›üy´Ü´Ú´ÛÔÜÙàìàïéß¥ƒVŽm”e”x”€ê¿™«™çš–žUž£Ÿä·‰¸U¸ZºeºxÇˆÒ{Üfägè‰´ß´à´Ý´ä´Þ´ãË¥´á´âè­ßýã²ÝÍë¥éÁtºÌå‚yƒþ†Ÿ‰…²ìŒœéõ‘N—½˜§yûŸnŸÕª‰¬X¯Q°„´…¸W»‚¼¾\¿\¿…ÀŠÁŒÃyÃœÄƒÄ‹Ä›ÒPÚ~ÛnçJö¿îx´å´ç´æ¶×ââñåü„Y…¼‰–’Ž›–¿£´¸€»vÛZß—´í´é´ê´ì´ë´èáÏØÈõºëâðîõãðûï±ïó„v„z‰èÕŽó´ÝÎô×î— Ì I¬›±‘¿WÇsÇuÉcÉxÊPÌ‘ÒPÕ‹õòÜgßHßuàŸáAáiäSåeóqûzý€',"+
			"d':'´ó´ð´ï´ò´î´ñËþóÎÞÇßÕñ×ðãâò÷°æ§í³àª÷²‡„‘…A…ì…ö‡}ˆ™Ëú‰¡‘„“‚“Ò™\šÎšùœÍžØ [®}®†±o³K¸—ÀJÁeµ¨ÇEÇQËRÏƒÓuÔzÛQÜJÞ…Þ‡µüßQß_æ]æpèNí^ý‘ý“´ø´ú´ô´÷´ý´ü´þ´õ´ûµ¡´ö´ó´ùß¾çéåÊá·ß°÷ìææçªÜ¤þ…¦‡Nˆ‚Ž‘Ž¡Ž§K‘·•Î–±é¦¶¾šùžŽªy¬x¹yº‰½H¿DÅ•ÊOÍfÎ}ÏEÒyÔrÚ±ÙJÛFÛÜÜ–ÝDÞaåÖßfßrÁ¥ìOì^ñWñjñ~ÍÔõ\ølünµ«µ¥µ°µ£µ¯µ§µ¨µ­µ¤µ¢µ©µªµ®µ¦µ¬Ê¯ðãå£ð÷ÝÌééíññõóìêæÙÙà¢S·‚„ƒdƒ{ƒÑÈ½„[„é…S…g…ì†m†›†²†Î‡d‡n‡~‡·Ì³‰¯ŠlŠ½‹[ŽŽ—³Àâò´×‘„‘žº¶’b“Ú“Û“ú–½éÜšKš—›X›Õ¿Ì¶å¤ÚŸí ý«m­®X¯D°D°Q°œ³N¶V·žº„¼ÀWÂnÂ›ÄEëþÄ‘ÍžÑÑÏ€ÐyÑÒRÒbÒ—êèÓgÓ”Õ²ÕQÖÙœÙ ÉÄÛÜlàáGá]ìKîFðZð…ñdñšó‡ø}ü^ülürÚàØéµ±µ³µ²µµµ´ÚÔîõå´ÝÐÛÊñÉí¸‚«ƒ}‡ŽˆW³¡ˆ›ˆö‰³‹P¤ÉÕ“õ”†™n™éšë‹ÇžªÌÌ C«š¬„­T­c®G®”¯ƒ±U²^´XµD¹Yº‚ºšÅ™ÊŽÌoÏ}Òd×[×•ÚßTèKêWë‹ühµ½µÀµ¹µ¶µºµÁµ¾µ·µ¿µ¼µ¸µ»àüôîâáìâë®ß¶Ù±ƒ‰ÊÜßú†ý‡‹‰»ŒpŒ§ŒàuëìŽWŽÎìýã°’Ò“v”F–]—Í˜˜™|™„ä¬ÌÎý c­±Iµ”¶\·R¹|½rÁŸÂRÅsÈKËgÍ@ÐmÐpÑnÜ„á~á’ê‰ÌÕëIëZô€÷øBÄñØÖµÄµØµÃµÂµ×ï½‡NÔzœ¿—‘›úµÇåuÚìµÃ†O“g’O’YµÈµÆµËµÇ³ÎµÉµÊµÅíãïëàâáØê­ô£ƒ\‰œ‹¿‘~³È™žŸô­O¸~Å˜ÓRØOà‡ç‹ëQµØµÚµ×µÍµÐµÖµÎµÛµÝµÕµÜµÞµÌµÄµÓÌáµÑµÏµÒµÔµÙêëÛ¡ÚÐÚ®àÖèÜ÷¾ôÆØµé¦íûæ·Ý¶íÚïáÛæÙáíÆª‚d‚±ƒCƒ™É×…}…à†v††¬à´‡”ˆhˆkˆªˆ¯ˆ¹‰y‰„‰—ŠD‹XÞŽRftw~µK‘d’F’†’ã“W“Ÿ”³•Aè¼–m–š—\—b˜N˜µ›ÁœvœìŸb ¹«Z«Ÿ®S¯F±ƒ´Y´”µ¶Eºa¼e¼s¾†Ô¼Â‚ÃJëÕÄVÉÖÆlÆmÝ¯Ç…ÇœÉ‰ÊHÊLÊOËyË‹ÍhÎ[ÏEÐ”Ó]ÓhÔgÕœÖBØpÚdÚhÛqÛyÌãÛ‡Û—ÝBÖðÞž´þßfßmßrâKãdå~çCêsêÁ¥ì{íLîEî}ÌâñVóƒóžô†öWûMàÇµãµçµêµîµíµàµßµæµâµëµìµäµèµåµáµéõÚîäÛãÚçñ²ô¡çèáÛñ°×‚Ù…Ž†ˆÛþ‰|‰«ŠHŠû‹LÑŽoŽp‘úÄé”„”“”¥—Ï˜ˆ˜•˜ë™AÕ´ÏÑœ¶Õ¬U¯t¯’°d´ÄHÉ_ÊsòÑÍŸÔaÛ†âšëŠîFîŒîò›ücý‚Ø¼µôµöµðµõµñµ÷µóµïµòÄñîöï¢öôõõ®Ù¬ÙÃµ¶„aŠP‹àŒÅt‡¬’FÌô—¹š“šôœ@¬h¯š²f³H³í·–¸L¸uºyôÐ¼g½r¾I³ñÝ¯É‰ËyÍ@ÍqòèÓŽÕAÕ{ÕÔÚwÌøõÖÝUé÷ážâyäHä”åcèSëïMæôô†õMõ øBøJù@ùmûbü—µùµøµþµúµûµüµýëºÜ¦ð¬ÞéõÞñóöøÛìà©Øý†A†O†—U«ÞŽ²LgÂ‘ä’”’¡•i•è–»˜G˜›šŠšÛ›uÉæœhäÍ š®’®¯A¯B±y±‚ÖÏ½xÀ„ÂWÃ]ÖÁÅ\ÅŽÆ|ÎHÏHÑñÞÒBÔeÕ™ÚgÛ@ÛLÌßÛÝWéóç“èFéPíCõ]ölö÷£õÚ‡Ã¶¥¶¨¶¢¶©¶£¶¡¶¤¶¦¶§î®çàîúëëíÖðÛØêñôôúà¤µìŠcàŽŠâ’ð—ÅÍ¡ìµÆ®k³G´O´ÂˆÆJÈbÝãËYÍBÓ†á”äbåVç–ìwí”îrï}ð—¶ªîûGäAïM¶¯¶«¶®¶´¶³¶¬¶­¶°¶±¶²á¼ð´ÛíëËëØíÏë±á´ßË‚”ƒPƒö„Ó„çˆÄ‰’ŠŸŠà‹Ùd–ž‘ã’œ“_•k–|Í©—šæ›òœ§žúŸüªJð®¸•Í²¹c¹š½pÄLÆ{Ç‡ÊÎXÐhÔ˜Õ‰Þ“ÍªëšñŽòLõ[öCù…úHüŠâº¶¼¶·¶¹¶º¶¸¶¶¶»¶µ¶Áò½ñ¼óûÝúc‚JƒÃ„E„r…Ê†tÍ¶”Ô–’—u™XšÃšÑ›ÃäÂž^²fñ¾¸]Ã–ÅÇW×xÓâàKáHõ¢â^äWî×ékêLêhðLðôYôZô^ô`ôa¶Á¶È¶¾¶É¶Â¶À¶Ç¶Æ¶Ä¶Ã¶Å¶½¶¼¶¿¶Ê¶Ùó¼óÆà½äÂèüë¹÷ò÷ÇÜ¶ƒ™„E„†„‹…X… ‡€ÍÁ‰TŠ‹óÕ¬Žª”¾•’•¤˜Ì˜ÐéÒ™³š˜šœ›èž^ © Ùªš¬o­{°²G¶Š¸]óÃ¸‰ºVôîÇTÎ}Î–ÐCÑtÒeÒlÓGÔŒÕi×x×˜²ïØKÙ€ÚGá`åLåƒæNèoÕàêAê^ê•ì|íbí~íîDòyüt¶Î¶Ì¶Ï¶Ë¶Í¶Ðé²ìÑóý‚Ç„Œ‰F‹eåè”àš¬¬‡´Vº@»f¾„ÂZÄaÈ˜ÑƒõßÜYå‘æH¶Ô¶Ó¶Ñ¶Ò¶ØïæíÔí¡í­ƒµƒ¶ˆŒˆÍ‰[¶áŠZŒŒ¦Å‘‡‘»“€–€žAžSžwž}¯y´qµq½˜Ä„ËcÖd×B×m×·âqäJä„åTæmç…çŽÈñêŒê îXø‹c¶Ö¶Ù¶×¶Õ¶Ø¶Û¶Ü¶Ú¶Ý²»õ»ãçíïïæíâìÀí»¯¿¡‡‰•‰ÝŽÝ÷ª‘‡“Ç“æ˜J˜ú—Ÿõ Ôª–´]ÄRÄ]ÎPëàÛvÜHÜOÞšßqâgç…çŽîDò—¶à¶ä¶á¶æ¶ç¶â¶å¶è¶é¶Þ¶ßÍÔ¶È¶ãõâãõßÍîìñÖßáç¶šƒµƒ¶¶Ò„A„m„„„‹…¼†Æ‡š‡¾ˆ‘ˆÊ‰™‰š‰ïŠZŠb‹s‹µŒ¹“ü‘†’–’—´·´§”Ÿ”£”¦”­–\ÔÓ–m–šèÞ–ª–Ã–úé¢—Ù™EšÇ›kãûÉ¯k³›¾EÆ–ÑEÔqÕBØyÚrÛFÛGÛTÜoÜ€µ¦àâ‡åTæNèIÕàÍÓêwêyËåëDï˜ð™ñWñjôDõyùzüc',"+
			"e':'¶öÅ¶¶î¶ì¶ê¶ó¶í¶ï°¢¶ô¶ë¶ð¶ñ¶ò¶õï°ÚÌÛÑïÉãÕÝàÜÃéîæ¹Ý­öùò¦ëñãµßÀØ¬ðÊåíÑÇ„†Î±àÙ¨‚­‚Îƒ^ƒi„þ…\…v…Å…Ù†@†HÑÆ†s†‘°¡ßý†¡‡f‡Ù‡êÛëˆºˆ×ˆìˆñŠŠŠŽŠ´ŠâŠã‹jŒßŒïSk¬âÖŽþ™º‘ö“t“~“”AêÂ–•—¿™ÄšGšd´õšx›¡›áœŠ«M«¬c¯u°x±“³S³X³b³j³ríÒ´dµJ°·ÉJÊ‚ÌFÍLÎYÓFÓžÔ›ÕMÖ@×F×†Ø`Ü—ÝQÝ‘Þˆß]ß{ÒØâeä~åŠèyépé‘êiêq°¯îOîPî~î€ðIð_òFØªô‰ôŠötö÷{ø‘ùEùZù[ù˜ýLý|ý…¶÷ÞôÝìàÅŠCWŸ¸ð†ßííE˜s™ë”ñ¶ø¶þ¶ú¶ù¶ü¶û·¡¶ýçíöÜð¹Ù¦åÇîïõƒ¹ƒº„n…þ†„‹èŒ©ŒªXpr–k–é–ê˜Þš¾›˜œxå¦ –»•ÂYÂxÃsÄžÇHËnÐ^ÑLÔ Ù@ÙEÚÝ[Ý‰ÞWßƒãsêzê—ëXðDñ“ó’ó“õbø',"+
			"f':'·¢·¨·£·¥·¦·¤·§·©ÛÒíÀá‚ëŠ‘U‘°Î²¦“Ü–ì˜ìšø›o·ºžž¬m¯V°k°l²X¸ŸÁPÁUÆžÊ†ËtÙH±ááeáwåzéyóŠóŒ·´·¹·­·¬·¸·²·«·µ·º·±·³···¶·®·ª·¯·°ìÜÞÀî²Þ¬õìèóá¦¢³„F„G„å…K‡h‰“ŠiŠï‹Ë‹Ì‹Ñé‘Œ’BÞÕ”ó”õ–i–¯—¡—÷˜õšïšøœtJž~ž’Ÿ© í­[®‰±Fµ\¹B¹D¹ »O»o¾u¿œÁ€Ä‡ÅtÅwÅxËXó´Ï›ñÈÒTÓŒØœÜÝGÞNÞxâCçxïcïxïˆï‰÷Yú‹ë¶áë·Å·¿·À·Ä·¼·½·Ã·Â·»·Á·¾îÕáÝÚúèÊô³öÐ‚ØÎˆªˆÚ”ë•P•X•\›PœE °­œ±f±}µp¼ÍKÔLÚ“â[åpë„ó„ô™ö„÷›øhúJ·Ç·É·Ê·Ñ·Î·Ï·Ë·Í·Ð·Æ·Ì·Èóõòãëèìéåúì³áôÜÀã­ïÐö­ôäé¼äÇöîç³ðò‚n„|…Š‰ŠOŠóŠôŒÐŽüUâö·÷•h•›•Õ–F–{–É—’˜ì™J™¶œdžO éªU¬i¯X°CíÉìð¹A½E¾pç¨Ã^ÃcÃdÆ…ÜØÈQÊ„Ê†ÊˆÎNÏnÅáÑpÑqÒUÕuÙMçšêŠì]ìqïwïyð[ñIòWòaó‘öEü”ü–·Ö·Ý·Ò·Û·Ø·Ü·ß·×·Þ·à·Ó·Ù·Ô·Õ·Úèûå¯ö÷çãÙÇ÷÷ƒf·ËÅç‡Šˆbˆe‰ž±¼Š^Š}ŒðŽŒŽËkíª‘°çÞÕ”••S–B–D–Œ—r—±™Jš\åžÇŸøŸþìÜªŠÅÎ²b³W¶l¸j¼S¼ŠÁiÁ‚Á‰ÃRÄÈ†ÉkÊˆÍ_Í`ÐvÓŸØkØrÙSêÚÜmÞMâpä—èMëVëƒîC°äðiðñBñOôš÷aøXüRüvü‹·ç·â·ê·ì·ä·á·ã·è·ë·î·í·ï·å·æ·éí¿ÙºÛºÝ×ããßô§‚ªƒt„K„N„Oˆ©ˆù‰âŠ~Œ›o¥’¸Åõ“ž—Q—÷™l›h·º›Íœtœ˜œ½mž–žÐŸuŸ‘ŸÔ Èªh¬S®g¯‚±`´^ºA½ ¿pÃTÅ}Å‚ÇlÌXÌt°öÒƒÖSØNØSÙˆÚRåÌà•ähæ‘çQìbïLïpñTøLøPøiùiÅôüKÒ…·ð–¦ˆu—‚·ñó¾²»ˆ¡Švžä¼€ÀŒÀÆ]Ð[ë€ø]¸±·ù·ö¸¡¸»¸£¸º·ü¸¶¸´·þ¸½¸©¸«¸°¸¿·÷·ò¸¸·û·õ·ó¸³¸¨¸®¸¯¸¹¸¾¸§¸²·ø·ô·ú·ð·ý¸µ¸¼¸¥¸¢¸¤ÊÐ¸¦¸ª¸¬¸­¸·¸ÀíëíÉÜÞõÃõÆò¶ÜÀöÖá¥ÜòäæòÝÞÔÝÊòðöûòóç¦ç¨êçî·ïûÙëôïÙìèõÝ³æÚð¥æââöìðß»Û®Ü½åõíê²»T½ö¸‚Y‚a‚¾‚¿ƒåƒì„_°üß¼…ò†b²¸‡`ˆ}ˆŽˆ¡ˆóŠmŠ•ŠÂŠï‹D‹c‹Ë‹ÑåµŒ @TŽˆŽ“}áÜ·Í»³N‘Ê’h’½’ÑÞå“á”ê–Ž–¢–´–Á–Â–ó—­—Ó—Ú˜_›L›^·Ð›Š›šäßºžÞŸJŸr«c«s¬M­o®i®t®w®}¯ž±G³Qµy¶O¶·J¸c¹A¹[¹r¹…º…»™¼J¼”¼›½E½n½•½š¾”¿`ÀbÁJÁÃiÄwÅ€Æ]Æ…ÇCÇXÆÎÈQÈiÈƒÉ’ÊÌ’ÍbÍkÍ|Í—ÎlÐuÐ“Ð•Ñ}Ñ‡ÒLÒiÒ„Ó‡ÔcÖDØfØ“ÙMÙxÙŽ·ÑÛ~ÝPÝoÝ•Ý—»¹ßß‘àGàMà~áKáUáœâaãRãVäžå‡å˜ïÂÚâê‚÷¹íhíví‚î\ïOïTñ€ó‘ôfõHõVõvövøDøIøWøqùfù›ûŸüAüF',"+
			"g':'¸Á¸ì¼Ð¸Â¿§ÔþîÅÙ¤ê¸ÞÎæØæÙßÈ‡Q«VÜˆáåmôp®h¸Ã¸Ä¸Ç¸Å¸Æ½æ¸Èê®ÛòØ¤Úëêà_ì„÷„ø¿ÈŠ¡Yã“©•|–qºË˜¢˜£Æû[­y®„´oµ‹½i½wëÜÇDÈ‘ÉwÔ“ØdÙWÙ^à@â}æYéuºÒêdëBº¡¸Ï¸É¸Ð¸Ò¸Í¸Ê¸Î¸Ì¸Ë¸Ó¸Ñêºôûí·ðáãïÜÕß¦ç¤éÏä÷äÆÞÏÛá¸öqÇ¬xœÎ‚‰ƒ÷„Q…î¼éŒ¼Œ¿ŒÀŽÖå’Iº´”—U˜o™gº¹›N›¿lž¸«\«q°‘±Y¶’¹C¹mºTº•»ˆ½CÆQÍHÐrÔlÖPØJÚCÚMÚsÞ|âFåDïó_ôvöx÷h÷ øN¸Õ¸Ö¸Ù¸Û¸×¸Ú¸Ü¸Ô¸Ø¿¸óàî¸í°¿ºØøƒé„‚ˆÕˆþŒù‘Þ‘ß¿¹’â—ž˜œÏŸ€ ± Â è¯I³M´L¾VÀ“À °¹âGä“æsêlî@ñþ¸ß¸ã¸æ¸å¸à¸Ý¸á¸â¸ä¸ÞÛ¬Ú¾ê½çÉØºéÀï¯éÂÞ»„Æ…Ì¾Ì‰ùz•±˜‚˜°™R™™²ºÆœõ»ª‚ªˆ°w²Gµ†µ‡¶J¶Ž·X¹l¿cÁoÅVÇÝïË›Õaä†æ€ízðpó{úkúüŽ¸ö¸÷¸è¸î¸ç¸é¸ñ¸ó¸ô¸ï¿©¸ì¸ð¸ò¸ê¸ë¸í¸ÇÒÙºÏ¸õíÑ÷Àò¢ñËÜªò´ÛÙïÓØîô´ØªàÃëõë¡æüÛÁ½éÞà‚€„ý¿É…Ã…Ï†þ‡S¸ÁÍ‘á‘ë’M’š”R”š–q˜†™ ºÆœèœð» ³ · çªnª˜íÀ¶…¹w¼vÃIÄ—ÅZÆŒºÊÉwÍxÑ\ÓkÔ†ÖYÖgÝ‘ÞPâ›ãtãxædækæŠ¼ØîþéléwéxÕ¢ì‘íRíkíuîMòZ÷ÄôŸõiõsö÷…ømøwøùB¸ø¸ú¸ùßçÝ¢Ø¨ôÞ“^“j¸ü¸û¾±¹£¹¢¸ý¸þ¹¡âÙöáßìç®ƒ¿º„jˆíya’ª’ù•œ—Ô›ÊŸ‰®uÓ²½b½c½Ž¾¿KÁ}ÇcÈ@ÙsÐÏàDàQîióiõ†ùˆûf†Ö†ñ¹¤¹«¹¦¹²¹­¹¥¹¬¹©¹§¹°¹±¹ª¹®¹¯¹¨ºìëÅö¡çîò¼…@…C…šßÛ†y†ß‰bŒmŽ³ÞÃã‘E’–r¸Ü–íœ|ŸË´bºT¼k¼tÁ‡ºçòËÓyØ•ÚCÚM¸ÓÜpÝ\äUì–ó•ô„ýŠý¹»¹µ¹·¹³¹´¹º¹¹¹¶¹¸¾äá¸ì°èÛ÷¸êíçÃóÑÚ¸åÜæÅóôØþ‚×ƒÚÇø…^…éˆx‰òŠ¥’]¾Ð“k“Â˜‹›tœÏŸµ«vº¾—ÂTÂUÂVÆ™ÍmÐÑÓMÔ_ÔØmØxÙÝ@âhã^ëgíxõLøzûYð¶¹Å¹É¹Ä¹È¹Ê¹Â¹¿¹Ã¹Ë¹Ì¹Í¹À¹¾¹Ç¹¼¹Á¹Æ¼Ö¹½èôð³ãééïáÄÝÔðÀ÷½îÜëûôþßÉöñÚ¬êôî­ì±ïÀêöðóõýòÁî¹ØÅ‚ïƒlƒó„½¸æßß†f†g†˜†Ø†åˆØ‰à‹²Œ½gHë’M’_¿Ý–¾—›˜b˜€™O™¤›}›ü»¬žJžkŸ‚ð­¸Þ°–±W³‘´hµ¶™·Y¸š¹‡ºH¼M¿SÁBÁlëÒÃ™ÅV¿àÆ‚ÉuË[ÍvÐM½ÇÔbÙZÝLÝMÝžßEââ’ådíî™ðkð ÷»õYöAøù]úXü‰¹Ò¹Î¹Ï¹Ñ¹Ð¹ÓØÔßÉëÒð»èéÚ´ƒÖ„Ž„œ…³†F†J†§ˆqã·’ìšOŸ…Ÿ°½\¾ ÁGÁLÉàÆ‚ÔŸÕ ÚoÛ|ã”äTèïNïWòmøŽÀ¨¹Ö¹Õ¹ÔÞâ…¨ßà‡ˆ‰øs–¡–Ê¹yÁL¹Ø¹Ü¹Ù¹Û¹Ý¹ß¹Þ¹à¹Ú¹á¹×ÂÚîÂñæÝ¸ÞèäÊ÷¤ðÙÙÄO´®…jŠþ ¡‘T‘×“¥ÎÓ¹ûèæ˜À™Â™àš¯ÂÙ›Œ›ýœS‚ ƒ¬g­¯p¯°H²•µeµ¸A¹`¾]À•ÅoÝÑÈXÉFÒ‹ÓQÓ^ØžÜIÝ„ßkå]æšè…érévêKêPëqð^öŠ÷b÷}øAùJûX¹â¹ã¹äèæáîßÛë×ï‚UƒZˆŠ­ŽÚV»ÐÀ©’•“Ñ”Uºá™¤™õ›²äêž»žÓžÕžÖŸD«E«‡³qÅQÅSÆšÚ‡Ý_Þ‚ã üU¹é¹ó¹í¹ò¹ì¹æ¹è¹ð¹ñ¹ê¹î¹ë¹å¹ç¹ô¿þ¹ïÈ²âÑå³èíØÛ÷¬öÙð§ØÐæ£êÐóþêÁwÎ±æ‚Îƒ^„£„¥…QØÑ…T…‘ÍÛˆ’Š¹ÍÞ‹‚‹¥‹¾Ž@ŽQŽ`Ž¢Žë@i“±“Ê”Š”‹•Q–_Î¦—Ë—Î¸Å˜œ˜­˜²˜³™u™™™Æ™ÍšwšðãíÍÝœˆœÄ‘«•­Y­„°I²Z²n²z³uÆíµƒ¶W·˜¹Kºl½}ÀL»æÃvÄ„Æ—É}ÌlÍŠÎšÏjÑOÒ^ÒŽõûÓmÔŽÖdÙFÚbÚ‘õêÜ‰ßžàFé|ê{ÚóëvíWòoôhôkõq÷Z÷iøWø_ø`ý”¹ö¹÷¹õöçÙòíÞçµØ­¨—œ»ë»ìœ†L¬g­e±š²O¾i¾É€ÊFÐ–ÑrÖÝåKï¿õPõ…öŠ÷¤¹ý¹ú¹û¹ü¹ø¹ùÎÐÛöé¤ñøÙåâ£áÆÞâàþßÃë½òäòå»®†F†J»£†©‡Hàí‡ë‡î‡ñ‡ó‡øˆÍˆå‰Ž½˜œ«‘I´ê“”šèÛ—ë˜¡™¤»î›ýœuXã¯†²Žºl¼@¾[ÂƒÄBÄNÄsÇ‘ÊbòâÎÏXÙùÑxÝ{ß^âuä˜åèJï¾ðRðŸ',"+
			"h':'¹þ¸òÏºîþRÏÅºÇŠUŠožéâ³Îrãx»¹º£º¦¿Èº¤º¢º§º¡º¥àËõ°ëÜì…õßÔ†ã‡¯‰h’™üŸQªn½wß€à@áVéuºÒîWò¤ï™ðŽñ”ñ›ºÙaº°º¬º¹º®ºººµº¨º«º¸º­º¯º©º²º±º³º´º¶º·ºªÚõÝÕÞþå«ãÛñüòÀìÊò¥êÏ÷ýœÎ‚þƒË„T³§…{…î†c†i‡•‡öˆ¥ŠÎ‹©Œå—²Ç¶å¸Ð’I”êº•~•ˆ•Â—U—c—ß˜o™÷ša›Nãï›¿›È›Û›þäÆÌ²hä÷¶Èž©Ÿß ’ªR¬H¸Ê®]°y±Ží·¸’¹b¼`ÃQÇtÊGÌkÍHÍ”ÎKÎLÎ‘Ö›ØEØJÜŽÐùâFâjäIädäwîÔé\êRê\ënìyíní™îMîhîuî›ñHñUòAô_õAøNú[ÐÐÏïº½º»º¼¿Ôñþãìç¬çñˆœŠsý”ãèì¿»ÀÇ¸‘¹V½W°¹ÆfÍaØ˜Þ†ß’ôûî@ôŒºÃºÅºÆº¿º¾ºÂºÁºÀºÄºÑ¸äê»ò«å°àãòºàÆð©Ýïå©Þ¶‚ÛƒŸ…ë†S»£‡_‡sæ¤hˆ•a•‰•±•µ•¼•Ø—·œBœéœõ»ž®ª|ª‚¸Þ°€°‚°…°ˆØº¶m¸h»DÂGÂ|ÄzÅVÆ’ËAË^ËrÌ–Ì—Ï–Õ’×qàzæeæ€çî—ö‚ºÍºÈºÏºÓºÌºËºÎºÇºÉºØºÕºÖºÐº×ºÊºÑºÒºÔÏÅàÀÛÀîÁôçãØò¢ÛÖÚ­æüêÂÞßÃºô…ô†J¹þ†Y† †¿†Ûà¾‡m‡˜ˆ†Šº¦ŒyP²Ô’u’š½Ò”—–­¸ñ—æšBšÎÇ¢œfœz¿Êœ¸¼ŸZŸŒŸ¿ŸÀ _ e çªC°F°±A±B¹è´E´¶…ºK»t»—¼vÀU½ÉÂG¿ÁÈMËrÞ½òÂÐ«Î˜ÏšÐŽÒ‡ÔXÔZÔ†ÖyØ€ÙRÝ`Ý éûÏ½àAãFèYéuêHãÒëa»ôìeìfìgíHîMïðgðšô]ôŸ÷…ù]ùŸúKúQûSðÀûiûýLý[ý†ý˜ºÚàË‹Ï¦ü\ºÙºÜºÝºÞºÛäßç‡Œ’‹ÏÆôÞÔ‹ì•ºáºãºßºâºàÐÐèìçñÞ¿ä†‘ˆýŠ¬a™M›êžîªBÃtÃ†ÙêèUø’ùCûaYbºìºäºåºçºéºêºæºèºëÚ§ÙêÞ®ãÈÞ°ÙäÝ¦ãü›…š…·…Æ…Ë†M†y†ß‡«ˆ˜ŠkŠ¼ŒfŒâŽcšã“E“Ð•{Íô›K›Ä›Í¸Ûœ|œ‚~µ¹ž¿Ÿp«Y«a­˜³{³…¸f¸sºC»Ž¼t¼‡¼˜½“À€Á‡ÁŠÁÂoÅ|ÆyÈ‡ÈˆÓÖhØAØDØFÜŸÝ“ÞZâvãpäUäfåébé{é•é—ë”ëŸìô\ô„ø™üZºóºñºðºíºîºòºïö×óóÜ©ááåËô×÷¿ðú…Ë…éˆ‹Ž«›• ê²T³@ÂFÂJÄDÈ‰ÔÚ¸Ø_àCàjãæAðfõ`ö\÷c÷ýJºþ»§ºô»¢ºø»¥ºú»¤ºý»¡ºöºüºûºù»¦ºõÏ·ºËºÍº÷»£ðÉÙüâïð×óËìæìè÷½ä°ìïõ­çúàñìÃéõð­ìÎõúðÀâ©ã±á²äïì²ßüéÎ[‚sƒê…I…O†¼†Ø†Û‡F‡P‡©ˆ~‰Ö‰ØŠ¯Šý‹|‹¬‹­ŒŒŽŽÄuHm‘ï‘ñ‘ò‘ô‘õ’_“‡“ª”N•O•U•÷–—ü—ý˜«šXš£ãé›R›Z›~›´›üœWœXœûGžCž€ŸWŸÚ­•®@µC·‚¹}¹”ºn»‡½`½œ¿S¿T¿eÓðëÒÄŠÅnÆSÆUÂ«ÜÌ¿àÆ~ÈLÊSÊdò®ÌÌ•Î™ÐkÓ{ÔSÖ—×oÐíØmÜ à‚â’änåtåæLîÜëa¹ÍëiëŒí_í’îgðbôEô–öUö{÷s÷ŸøUø‡ù]ù–úCúKúXûI»°»¨»¯»­»ª»®»¬»©»«»íîüèëæèí¹Ù¨„Øå…ÅÍÛ‡WˆµŠ£‹N‹O‹½‹ÃÑ§ŒW†ÕÒ“Š“®“çµÐ–—É˜¥˜å™Šä«±Òªœ­L®‹®“³“´hïý¼@¼AÀEÄBÅpÆ_ÈAÉJÊyÌfÌsÎ”ÓiÔ’ÕjÕ–Õ Öœ×fÝ{âDâEänåkçfò‘ô‰õqöÙú†üX»µ»³»´»±»²»®õ×Ý†Fà°‡]ÛÚÅ÷‰²‰Ä‘¯‘Ñ™ÆžxÂjÌxÌ|Ñ‘Ñœ»»»¹»½»·»¼»º»¶»Ã»Â»Á»À»¿»¸»¾äñâµß§åÕöéÛ¨÷ßå¾Û¼ïÌà÷ä¡ÝÈçÙä½†¾†¿‡È‡õÛùˆâŠJŒAŒ~`´ŽwÑ‘¤‘×“QÔ®”k—h˜¬™öšZšgš÷œo‚È×¹àžðŸ¨ íªB¬~è¥­h­’¯ˆÍîÑ£±±š²`²o¼]½b½Œ¾ÀQÁvÃKëäÇBÈPÉVËÎŒÐS×’ØhØoØ}ØŽÝkÞSß€à ãhæDèGéIêXêaëfëqóOõŒöZödøbùJûXûqðÙ»Æ»Å»Î»Ä»É»Ë»Ê»Ñ»Ì»È»Ç»Ð»Í»ÏÚòëÁäêóòáåöüåØñ¥äÒó¨è«‚µƒÆ†Åˆð‰E‰ŸŠN‹hŒr¢ŽxUé“N•s•Í–M˜R˜n™¤›R›²œêžêŸºŸì pª¬‰°°Œ·k¿mÅŠÃ¢Ã£ÈÐYÔ…ÖWÖeÚ‡å–æwçuéBí‹ðcòböm÷UúŠüS»Ø»á»Ò»æ»Ó»ã»Ô»Ù»Ú»Ý»Þ»Õ»Ö»à»Û»ß»×»ä»²»Ü»â»ååçä«çõÞ¥à¹í£ßÜêÍãÄ÷âÚ¶ó³Üîä§ßÔò³ÜöçÀÝƒaƒª…R…¡‡G‡j‡v‡‚‡¤‡ß‡éˆH¶é‰™‰ÄŠî‹^Œ@Œ“ŒáŽ¹@hj¡¢Úo{‘}‘Î’’“]“Ö•Ÿ•Á•þèí—Û—ò˜ž™B™b™m™u™®š§š«›i›x›‘»Áœ“œóŒÒèž`ž¾Ÿ@ŸCŸFŸŸ˜ S Zª›¬q­_­g¯`¯ð©íõî¡²N²~µ˜·xº_½}ÀDÀLÁ™ÁšÂEÂPÆUÉLÊ]ËCËDË™ÌlÌs³æÍYÍzÍ ÎšÐ„Ñ‹Ò^ÔÔœÕdÖM×M×e×f×wØYÙVÝxÝ{Î¥Þ’ßDß`ã„çiçžêTê_ëDìuíWífí}Î¤î_îœðdõtö™ýHýIÀ£»ì»è»ç»ë»é»êãÔçõâÆäãÚ»‚[‚“‡õ‹Gù¸Çù»ÓÀ¦’ä“]À¥•e—p—y¹÷—•š‰›÷œ†œ¡œ³Ÿ[Ÿk¬q±d²E²J¾i¾r¾‡¿ŒçµçÅÈÊMÓoÕŸÞFé’î‚ðQðaý@»ò»î»ð»ï»õºÍ»ñ»ö»í»ô»óàëïìñëØåÞ½ß«ïÁó¶îØâ·å‚i„Š»¯…¿…ô…ü†Ø‡—‡ÉŠ_Š£°ç’»’î“n”N”ü•ë—ë™Š›[œ­tèžCžmìáŸZ«@ð­°\±n²ˆ²‘´žµœ¶¶„·‚ºWÂhÄNÄsÅGÅŸÈuÉ^Õ’ÖfØmØ›Ô½Úoß^ß˜â€åxèZéXëbëoì[òdôr‰þ',"+
			"i':'U',";
		int stringbufflen = stringbuff.length();
		for(int i=0;i<20000;i++){
			string1.append(stringbuff.charAt((int) (Math.random()*stringbufflen)));
		}
		for(int i=0;i<2000000;i++){
			string2.append(stringbuff.charAt((int) (Math.random()*stringbufflen)));
		}
		InputStream fin = new BufferedInputStream(new FileInputStream("C:/1.dmp"));
		long finlen = new File("C:/1.dmp").length();
		short shor = (short) (Math.random()*Short.MAX_VALUE);
		int i = (int) (Math.random()*Integer.MAX_VALUE);
		long lon = (long) (Math.random()*Long.MAX_VALUE);
		Date d = new Date((long) (Math.random()*Long.MAX_VALUE));
//		d = new Date(14893489L);
		IOUtil.writeByte(out, byt);
		IOUtil.writeDate(out, d);
		IOUtil.writeInt(out, i);
		IOUtil.writeLong(out, lon);
		IOUtil.writeShort(out, shor);
		IOUtil.writeString(out, string1.toString());
		IOUtil.writeString(out, string2.toString());
		IOUtil.writeLong(out, lon);
		IOUtil.writeInputStream(out, fin, (int) finlen);
		out.close();
		
		
		
		InputStream in = new BufferedInputStream(new FileInputStream("C:/tmp.tmp"));
		byte byt2 = IOUtil.readByte(in);
		System.out.println("BYTE   "+Integer.toHexString(byt)+"=="+Integer.toHexString(byt2)+":"+(byt==byt2));
		Date d2 = IOUtil.readDate(in);
		System.out.println("DATE   "+Long.toHexString(d.getTime())+"=="+Long.toHexString(d2.getTime())+":"+(d.getTime()==d2.getTime()));
		int i2 = IOUtil.readInt(in);
		System.out.println("INT    "+i+"=="+i2+":"+(i==i2));
		long lon2 = IOUtil.readLong(in);
		System.out.println("LONG   "+lon+"=="+lon2+":"+(lon==lon2));
		short shor2 = IOUtil.readShort(in);
		System.out.println("SHORT  "+shor+"=="+shor2+":"+(shor==shor2));
		String str1 = IOUtil.readString(in);
		System.out.println("STRING "+(string1.toString().equals(str1)));
		System.out.println("STRING "+(string2.toString().equals(IOUtil.readString(in))));
		lon2 = IOUtil.readLong(in);
		System.out.println("LONG"+lon+"=="+lon2+":"+(lon==lon2));
		InputStream in2 = IOUtil.readInputStream(in).getIn();
		byte[] buff = new byte[1024];
		int len;
		BufferedOutputStream out2 = new BufferedOutputStream(new FileOutputStream("C:/2.dmp"));
		while((len=in2.read(buff))!=-1){
			out2.write(buff,0,len);
		}
		out2.close();
	}

	public static byte readByte(InputStream in)
			throws IOException {
		int i;
		if ((i = in.read()) < 0) {
			throw new EOFException();
		}
		return (byte) i;
	}

	public static String readString(InputStream in)
			throws IOException {
		int i;
		if ((i = readShort(in)) == 0) {
			return null;
		}
		if(i<0){
			int j = readShort(in);
			i = ((-i)<<15)+j;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buff = new byte[1024];
		int len;
		while((len=in.read(buff, 0, Math.min(i, buff.length)))!=-1&&i>0){
			out.write(buff,0,len);
			i-=len;
		}
		return new String(out.toByteArray());
	}

	public static short readShort(InputStream in)
			throws IOException {
		int i = in.read();
		int j = in.read();
		if(i==-1||j==-1){
			return 0;
		}
		return (short) ((i << 8) + j);
	}

	public static int readInt(InputStream in) throws IOException {
		int i = in.read();
		int j = in.read();
		int k = in.read();
		int l = in.read();
		if(i==-1||j==-1||k==-1||l==-1){
			return 0;
		}
		return ((i << 24) + (j << 16) + (k << 8) + l);
	}

	public static int[] readIntAry(InputStream in)
			throws IOException {
		int i;
		if ((i = readShort(in)) == 0) {
			return null;
		}
		int[] arrayOfInt = new int[i];
		for (int j = 0; j < i; ++j) {
			arrayOfInt[j] = readInt(in);
		}
		return arrayOfInt;
	}

	public static FixedInputStream readInputStream(InputStream in)
			throws IOException {
		InputStream fin = null;
		int i;
		if ((i = readShort(in)) == 0) {
			return null;
		}
		if(i<0){
			int j = readShort(in);
			i = ((-i)<<15)+j;
		}
		byte[] buff = new byte[1024];
		if(i<1024*1024){//Ð¡ÓÚ1MÊ±Ö±½Ó×öÎªÄÚ´æÖÐµÄbyteÊý×é¼ÓÔØÊý¾Ý
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int len;
			while((len=in.read(buff, 0, Math.min(i, buff.length)))!=-1&&i>0){
				out.write(buff,0,len);
				i-=len;
			}
			fin = new ByteArrayInputStream(out.toByteArray());
		}
		else{//³¬¹ý1MµÄÎÄ¼þÀûÓÃÁÙÊ±ÎÄ¼þµÄ»º´æ
			File f = File.createTempFile("read", "001");
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(f));
			int len;
			while((len=in.read(buff, 0, Math.min(i, buff.length)))!=-1&&i>0){
				out.write(buff,0,len);
				i-=len;
			}
			out.flush();
			fin = new BufferedInputStream(new FileInputStream(f));
		}
		return new FixedInputStream(fin,i);
	}

	public static long readLong(InputStream in)
			throws IOException {
		int i = readInt(in);
		long j = readInt(in);
		if(j<0){
			j = j&0xFFFFFFFFL;
		}
		return ((((long)i) << 32) + j);
	}

	public static Date readDate(InputStream in)
			throws IOException {
		if(readShort(in)==4){
			return new Date(readLong(in));
		}
		else{
			return null;
		}
	}

	public static void writeByte(OutputStream out, int b) throws IOException {
		out.write(new byte[] { (byte) b });
	}

	public static void writeString(OutputStream out, String str)
			throws IOException {
		if (str == null || str.length() == 0) {
			writeShort(out, (short) 0);
			return;
		}
		byte[] bs = str.getBytes();
		int len = bs.length;
		if(len>Short.MAX_VALUE){
			writeShort(out, (short) -(len>>15));
			writeShort(out, (short) (len&0x7FFF));
		}
		else{
			writeShort(out, (short) len);
		}
		out.write(bs);
	}

	public static void writeInputStream(OutputStream out,InputStream in,int len)
			throws IOException {
		if (in ==null || len == 0) {
			writeShort(out, (short) 0);
			return;
		}
		if(len>Short.MAX_VALUE){
			writeShort(out, (short) -(len>>15));
			writeShort(out, (short) (len&0x7FFF));
		}
		else{
			writeShort(out, (short) len);
		}
		byte[] buff = new byte[1024];
		int l;
		while((l=in.read(buff,0,Math.min(len, buff.length)))!=-1&&len>0){
			out.write(buff,0,l);
			len -= l;
		}
	}

	public static void writeShort(OutputStream out, short s) throws IOException {
		out.write(new byte[] { (byte) (s >> 8), (byte) (s & 0xFF) });
	}

	public static void writeInt(OutputStream out, int i) throws IOException {
		out.write(new byte[] { (byte) (i >> 24), (byte) ((i >> 16) & 0xFF),
				(byte) ((i >> 8) & 0xFF), (byte) (i & 0xFF) });
	}

	public static void writeIntAry(OutputStream out, int[] ary)
			throws IOException {
		writeShort(out, (short) ary.length);
		for (int i : ary) {
			writeInt(out, i);
		}
	}

	public static void writeLong(OutputStream out, long l) throws IOException {
		writeInt(out, (int) (l >> 32));
		writeInt(out, (int) (l & 0xFFFFFFFFL));
	}

	public static void writeDate(OutputStream out, Date l) throws IOException {
		if(l==null){
			writeShort(out,(short) 0);
			return;
		}
		writeShort(out,(short) 4);
		writeLong(out, l.getTime());
	}

	public static boolean byteAryEquals(byte[] paramArrayOfByte1,
			byte[] paramArrayOfByte2) {
		if ((paramArrayOfByte1 == null) || (paramArrayOfByte2 == null))
			return false;
		if (paramArrayOfByte1.length != paramArrayOfByte2.length)
			return false;
		for (int i = 0; i < paramArrayOfByte1.length; ++i) {
			if (paramArrayOfByte1[i] != paramArrayOfByte2[i])
				return false;
		}
		return true;
	}

}