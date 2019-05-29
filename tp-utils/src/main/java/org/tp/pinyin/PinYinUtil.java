package org.tp.pinyin;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 拼音工具类
 */
@Slf4j
public class PinYinUtil {
    /** 日志工具 */
//    private static final Logger LOGGER  = LoggerFactory.getLogger(PinYinUtil.class);

    /**
     * GB2312汉字的首字母
     */
    private static final String GB_2312 = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabbbbbbbbbbbbbp"
                                          + "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbpbbbbbbbbbbbbbbbbbb"
                                          + "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb"
                                          + "pbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb"
                                          + "bbbbbbbbbbbbbbbbbbbbcccccccccccccccccccccccccccccc"
                                          + "ccccccccccccccccccccccccccccccccccczcccccccccccccc"
                                          + "ccccccccccccccccccccccccccccccccccccsccccccccccccc"
                                          + "cccccccccccccccccccccccccccccccccccccccccczccccccc"
                                          + "cccccccccccccccccccccccccccccccccccccccccccccccccc"
                                          + "cccddddddddddddddddddddddddddddddddddddddddddddddd"
                                          + "dddddddddddddddddddddzdddddddddddddddddddddddddddd"
                                          + "dddddddddddddddddddddddddddddddtdddddddddddddddddd"
                                          + "dddddddddddddddddddddddddddddddddddddeeeeeeeeeeeee"
                                          + "eeeeeeeeefffffffffffffffffffffffffffffffffffffffff"
                                          + "ffffffffffffffffffffffffffffffffffffffffffffffffff"
                                          + "fffffffffffffpffffffffffffffffffffgggggggggggggggg"
                                          + "ggggggggggggggggggghggggggggggggghgggggggggggggggg"
                                          + "gggggggggggggggggggggggggggggggggggggggggggggggggg"
                                          + "ggggggggggggggggggggggggggggggggggggggghhhhhhhhhhh"
                                          + "hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhmhhhhhhhhhhh"
                                          + "hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh"
                                          + "hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh"
                                          + "hhhhhhhhhhhhhhhhhhhhjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj"
                                          + "jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj"
                                          + "jjjjjjjjjjjjjjkjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj"
                                          + "jjjjjjjyjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj"
                                          + "jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj"
                                          + "jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj"
                                          + "jjjjjjjjjjjjjjjkkkgkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkh"
                                          + "kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk"
                                          + "kkkkkkkkkkkkkkklllllllllllllllllllllllllllllllllll"
                                          + "llllllllllllllllllllllllllllllllllllllllllllllllll"
                                          + "llllllllllllllllllllllllllllllllllllllllllllllllll"
                                          + "llllllllllllllllllllllllllllllllllllllllllllllllll"
                                          + "llllllllllllllllllllllllllllllllllllllllllllllllll"
                                          + "lllllllllllllmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm"
                                          + "mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm"
                                          + "mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm"
                                          + "mmmmmmmmmmmmmmnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn"
                                          + "nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnooooo"
                                          + "oooppppppppppppppppppppppppppppppppppppppppppppppp"
                                          + "pppppppppppppppppppppppppppppppppppppppppppppppppp"
                                          + "ppppppppppppppppppppppppbqqqqqqqqqqqqqqqqqqqqqqqqq"
                                          + "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq"
                                          + "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq"
                                          + "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqrrrrrrrrrrrrrrrrrr"
                                          + "rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrsssssssss"
                                          + "ssssssssssssssssssssssssssssssssssssssssssssssssss"
                                          + "ssssssssssssssssssssssssssssssssssssssssssssssssss"
                                          + "ssssssssssssssssssssssssssssssssssssssssssssssssss"
                                          + "ssssssssssssssssssssssssssssssssssssssssssssssssss"
                                          + "sssssssssssssssssssssssssssssssssssssssssssssssssx"
                                          + "sssssssssssssssssssssssssssttttttttttttttttttttttt"
                                          + "tttttttttttttttttttttttttttttttttttttttttttttttttt"
                                          + "tttttttttttttttttttttttttttttttttttttttttttttttttt"
                                          + "tttttttttttttttttttttttttttttttttwwwwwwwwwwwwwwwww"
                                          + "wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww"
                                          + "wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww"
                                          + "wwwxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxsx"
                                          + "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
                                          + "xxxxxxxxxxxxxxxxxxxxxjxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
                                          + "xxxxxhxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxcxxxxxxxxx"
                                          + "xxxxxxxxxxxxxxxxxxxxxxxxxxyyyyyyyyyyyyyyyyyyyyyyyy"
                                          + "yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy"
                                          + "yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy"
                                          + "yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy"
                                          + "yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy"
                                          + "yyyyyyyyyyyyyyyyyyyyyyyyxyyyyyyyyyyyyyyyyyyyyyyyyy"
                                          + "yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyzzzzzzzzzzzzzzzzzz"
                                          + "zzzzzzzzzzzzzzzzzzzzzczzzzzzzzzzzzzzzzzzzzzzzzzzzz"
                                          + "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz"
                                          + "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz"
                                          + "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz"
                                          + "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz"
                                          + "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz"
                                          + "zzzzz     cjwgnspgcgnesypbtyyzdxykygtdjnnjqmbsjzsc"
                                          + "yjsyyfpgkbzgylywjkgkljywkpjqhytwddzlsymrypywwcckzn"
                                          + "kyygttngjnykkzytcjnmcylqlypysfqrpzslwbtgkjfyxjwzlt"
                                          + "bncxjjjjtxdttsqzycdxxhgckbphffsswybgmxlpbylllhlxst"
                                          + "zmyjhsojnghdzqyklgjhsgqzhxqgkxzzwyscscjxyeyxadzpmd"
                                          + "ssmzjzqjyzcdjzwqjbyzbjgznzcpwhwxhqkmwfbpbydtjzzkxx"
                                          + "ylygxfptyjyyzpszlfchmqshgmxxsxjyqdcsbbqbefsjyhxwgz"
                                          + "kpylqbgldlcdtnmaeddkssngycsgxlyzaypnptsdkdylhgymyl"
                                          + "cxpycjndqjwxqxfyyfjlejpzrxccqwqqsbzkymgplbmjrqcfln"
                                          + "ymyqmtqyrbcjthztqfrxqhxmqjcjlyxgjmshzkbswyemyltxfs"
                                          + "ydsglycjqxsjnqbsctyhbftdcyjdjyyghqfsxwckqkxebptlpx"
                                          + "jzsrmebwhjlpjslyysmdxlclqkxlhxjrzjmfqhxhwywsbhtrxx"
                                          + "glhqhfnmgykldyxzpylggtmtcfpnjjzyljtyanjgbjplqgszyq"
                                          + "yaxbkysecjsznslyzhzxlzcghpxzhznytdsbcjkdlzayfmytle"
                                          + "bbgqyzkggldndnyskjshdlyxbcgyxypkdjmmzngmmclgezszxz"
                                          + "jfznmlzzthcsydbdllscddnlkjykjsycjlkwhqasdknhcsgaeh"
                                          + "daashtcplcpqybsdmpjlpzjoqlcdhjxysprchnwjnlhlyyqyhw"
                                          + "zptczgwwmzffjqqqqyxaclbhkdjxdgmmydqxzllsygxgkjrywz"
                                          + "wyclzmssjzldbydcpcxyhlxchyzjqsfqagmnyxpfrkssbjlyxy"
                                          + "syglnscmhcwwmnzjjlxxhchsyzsttxrycyxbyhcsmxjsznpwgp"
                                          + "xxtaybgajcxlysdccwzocwkccsbnhcpdyznfcyytyckxkybsqk"
                                          + "kytqqxfcwchcykelzqbsqyjqcclmthsywhmktlkjlycxwheqqh"
                                          + "tqkjpqsqscfymmdmgbwhwlgsllystlmlxpthmjhwljzyhzjxht"
                                          + "xjlhxrswlwzjcbxmhzqxsdzpsgfcsglsxymqshxpjxwmyqksmy"
                                          + "plrthbxftpmhyxlchlhlzylxgsssstclsldclrpbhzhxyyfhbb"
                                          + "gdmycnqqwlqhjjzywjzyejjdhpblqxtqkwhlchqxagtlxljxms"
                                          + "ljhtzkzjecxjcjnmfbycsfywybjzgnysdzsqyrsljpclpwxsdw"
                                          + "ejbjcbcnaytwgmpapclyqpclzxsbnmsggfnzjjbzsfzyndxhpl"
                                          + "qkzczwalsbccjxjyzgwkypsgxfzfcdkhjgxtlqfsgdslqwzkxt"
                                          + "mhsbgzmjzrglyjbpmlmsxlzjqzhzyjczydjwfmjklddpmjegxy"
                                          + "hylxhlqyqhkycwcjmyyxnatjhyccxzpcqlbzwwytwsqcmlpmyr"
                                          + "jcccxfpznzzljplxxyztzlgdltcklyrzzgqtkjhhgjljaxfgfj"
                                          + "zslcfdqzlclgjdjcsnzlljpjqdcclcjxmyzftsxgcgsbrzxjqq"
                                          + "ctzhgyqtjqqlzxjylylncyamcstylpdjbyregklzyzhlyszqlz"
                                          + "nwczcllwjqjjjkdgjzolbbzppglghtgzxyjhzmycnqsycyhbhg"
                                          + "xkamtxyxnbskyzzgjzlqjtfcjxdygjqjjpmgwgjjjpkqsbgbmm"
                                          + "cjssclpqpdxcdyykyfcjddyygywrhjrtgznyqldkljszzgzqzj"
                                          + "gdykshpzmtlcpwnjyfyzdjcnmwescyglbtzcgmssllyxqsxxbs"
                                          + "jsbbsgghfjlypmzjnlyywdqshzxtyywhmcyhywdbxbtlmsyyyf"
                                          + "sxjchtxxlhjhfssxzqhfzmzcztqcxzxrttdjhnnyzqqmtqdmmz"
                                          + " ytxmjgdxcdyzbffallztdltfxmxqzdngwqdbdczjdxbzgsqqd"
                                          + "djcmbkzffxmkdmdsyyszcmljdsynsprskmkmpcklgdbqtfzswt"
                                          + "fgglyplljzhgjjgypzltcsmcnbtjbqfkdhpyzgkpbbymtdssxt"
                                          + "bnpdkleycjnyddykzddhqhsdzsctarlltkzlgecllkjlqjaqnb"
                                          + "dkkghpjxzqksecshalqfmmgjnlyjbbtmlyzxdxjpldlpcqdhzy"
                                          + "cbzsczbzmsljflkrzjsnfrgjhxpdhyjybzgdlqcsezgxlblhyx"
                                          + "twmabchecmwyjyzlljjyhlgbdjlslygkdzpzxjyyzlwcxszfgw"
                                          + "yydlyhcljscmbjhblyzlycblydpdqysxqzbytdkyxlyycnrjmp"
                                          + "dqgklcljbcxbjddbblblczqrppxjcjlzcshltoljnmdddlngka"
                                          + "thqhjhykheznmshrphqqjchgmfprxhjgdychgklyrzqlcyqjnz"
                                          + "sqtkqjymszxwlcfqqqxyfggyptqwlmcrnfkkfsyylybmqammmy"
                                          + "xctpshcptxxzzsmphpshmclmldqfyqxszyjdjjzzhqpdszglst"
                                          + "jbckbxyqzjsgpsxqzqzrqtbdkwxzkhhgflbcsmdldgdzdblzyy"
                                          + "cxnncsybzbfglzzxswmsccmqnjqsbdqsjtxxmbltxcclzshzcx"
                                          + "rqjgjylxzfjphymzqqydfqjqlzznzjcdgzygztxmzysctlkpht"
                                          + "xhtlbjxjlxscdqxcbbtjfqzfsltjbtkqbxxjjljchczdbzjdcz"
                                          + "jdcprnpqcjpfczlclzxzdmxmphjsgzgszzqlylwtjpfsyaxmcj"
                                          + "btzyycwmytzsjjlqcqlwzmalbxyfbpnlsfhtgjwejjxxglljst"
                                          + "gshjqlzfkcgnndszfdeqfhbsaqtgylbxmmygszldydqmjjrgbj"
                                          + "tkgdhgkblqkbdmbylxwcxyttybkmrtjzxqjbhlmhmjjzmqasld"
                                          + "cyxyqdlqcafywyxqhz";

    /** 扩展汉字拼音的首字母 */
    private static final String GBK_3   = "ksxsm sdqlybjjjgczbjfya jhphsyzgj   sn      xy  ng"
                                          + "    lggllyjds yssgyqyd xjyydldwjjwbbftbxthhbczcrfm"
                                          + "qwyfcwdzpyddwyxjajpsfnzyjxxxcxnnxxzzbpysyzhmzbqbzc"
                                          + "ycbxqsbhhxgfmbhhgqcxsthlygymxalelccxzrcsd njjtzzcl"
                                          + "jdtstbnxtyxsgkwyflhjqspxmxxdc lshxjbcfybyxhczbjyzl"
                                          + "wlcz gtsmtzxpqglsjfzzlslhdzbwjncjysnycqrzcwybtyftw"
                                          + "ecskdcbxhyzqyyxzcffzmjyxxsdcztbzjwszsxyrnygmdthjxs"
                                          + "qqccsbxrytsyfbjzgclyzzbszyzqscjhzqydxlbpjllmqxtydz"
                                          + "sqjtzplcgqtzwjbhcjdyfxjelbgxxmyjjqfzasyjnsydk jcjs"
                                          + "zcbatdclnjqmwnqncllkbybzzsyhjqltwlccxthllzntylnzxd"
                                          + "dtcenjyskkfksdkghwnlsjt jymrymzjgjmzgxykymsmjklfxm"
                                          + "tghpfmqjsmtgjqdgyalcmzcsdjlxdffjc f  ffkgpkhrcjqcj"
                                          + "dwjlfqdmlzbjjscgckdejcjdlzyckscclfcq czgpdqzjj hdd"
                                          + "wgsjdkccctllpskghzzljlgjgjjtjjjzczmlzyjkxzyzmljkyw"
                                          + "xmkjlkjgmclykjqlblkmdxwyxysllpsjqjqxyqfjtjdmxxllcr"
                                          + "qyjb xgg pjygegdjgnjyjkhqfqzkhyghdgllsdjjxkyoxnzsx"
                                          + "wwxdcskxxjyqscsqkjexsyzhydz ptqyzmtstzfsyldqagylcq"
                                          + "lyyyhlrq ldhsssadsjbrszxsjyrcgqc hmmxzdyohycqgphhy"
                                          + "nxrhgjlgwqwjhcstwasjpmmrdsztxyqpzxyhyqxtpbfyhhdwzb"
                                          + "txhqeexzxxkstexgltxydn  hyktmzhxlplbmlsfhyyggbhyqt"
                                          + "xwlqczydqdq gd lls zwjqwqajnytlxanzdecxzwwsgqqdyzt"
                                          + "chyqzlxygzglydqtjtadyzzcwyzymhyhyjzwsxhzylyskqysbc"
                                          + "yw  xjzgtyxqsyhxmchrwjpwxzlwjs sgnqbalzzmtjcjktsax"
                                          + "ljhhgoxzcpdmhgtysjxhmrlxjkxhmqxctxwzbkhzccdytxqhlx"
                                          + "hyx syydz znhxqyaygypdhdd pyzndltwxydpzjjcxmtlhbyn"
                                          + "yymhzllhnmylllmdcppxmxdkycydltxchhznaclcclylzsxzjn"
                                          + "zln lhyntkyjpychegttgqrgtgyhhlgcwyqkpyyyttttlhylly"
                                          + "ttplkyzqqzdq  nmjzxyqmktfbjdjjdxbtqzgtsyflqgxblzfh"
                                          + " zadpmjhlccyhdzfgydgcyxs hd d axxbpbyyaxcqffqyjxdl"
                                          + "jjzl bjydyqszwjlzkcdtctbkdyzdqjnkknjgyeglfykasntch"
                                          + "blwzbymjnygzyheyfjmctyfzjjhgck lxhdwxxjkyykssmwctq"
                                          + "zlpbzdtwzxzag kwxl lspbclloqmmzslbczzkdcz xgqqdcyt"
                                          + "zqwzqssfpktfqdcdshdtdwfhtdy jaqqkybdjyxtlj drqxxxa"
                                          + "ydrjlklytwhllrllcxylbw z  zzhkhxksmdsyyjpzbsqlcxxn"
                                          + "xwmdq gqmmczjgttybhyjbetpjxdqhkzbhfdxkawtwajldyjsf"
                                          + "hblddqjncxfjhdfjjwzpkzypcyzynxff ydbzznytxzembsehx"
                                          + "fzmbflzrsymzjrdjgxhjgjjnzzxhgxhymlpeyyxtgqshxssxmf"
                                          + "mkcctxnypszhzptxwywxyysljsqxzdleelmcpjclxsqhfwwtff"
                                          + "tnqjjjdxhwlyznflnkyyjldx hdynrjtywtrmdrqhwqcmfjdyz"
                                          + "hmyyxjwzqtxtlmrspwwchjb xygcyyrrlmpymkszyjrmysntpl"
                                          + "nbpyyxmykyngjzznlzhhanmpgwjdzmxxmllhgdzxyhxkrycjmf"
                                          + "fxyhjfssqlxxndyca nmtcjcyprrnytyqym sxndlylyljnlxy"
                                          + "shqmllyzljzxstyzsmcqynzlxbnnylrqtryyjzzhsytxcqgxzs"
                                          + "shmkczyqhzjnbh qsnjnzybknlqhznswxkhjyybqlbfl p bkq"
                                          + "zxsddjmessmlxxkwnmwwwydkzggtggxbjtdszxnxwmlptfxlcx"
                                          + "jjljzxnwxlyhhlrwhsc ybyawjjcwqqjzzyjgxpltzftpakqpt"
                                          + "lc  xtx hklefdleegqymsawhmljtwyqlyjeybqfnlyxrdsctg"
                                          + "gxyyn kyqctlhjlmkkcgygllldzydhzwpjzkdyzzhyyfqytyzs"
                                          + "ezzlymhjhtwyzlkyywzcskqqtdxwctyjklwqbdqyncs szjlkc"
                                          + "dcdtlzzacqqzzddxyplxzbqjylzllqdzqgyjyjsyxnyyynyjxk"
                                          + "xdazwrdljyyynjlxllhxjcykynqcclddnyyykyhhjcl pb qzz"
                                          + "yjxj fzdnfpzhddwfmyypqjrssqzsqdgpzjwdsjdhzxwybp gp"
                                          + "tmjthzsbgzmbjczwbbzmqcfmbdmcjxljbgjtz mqdyxjzyctyz"
                                          + "tzxtgkmybbcljssqymscx jeglxszbqjjlyxlyctsxmcwfa kb"
                                          + "qllljyxtyltxdphnhfqyzyes sdhwdjbsztfd czyqsyjdzjqp"
                                          + "bs j fbkjbxtkqhmkwjjlhhyyyyywyycdypczyjzwdlfwxwzzj"
                                          + "cxcdjzczlxjjtxbfwpxzptdzbccyhmlxbqlrtgrhqtlf mwwjx"
                                          + "jwcysctzqhxwxkjybmpkbnzhqcdtyfxbyxcbhxpsxt m sxlhk"
                                          + "mzxydhwxxshqhcyxglcsqypdh my ypyyykzljqtbqxmyhcwll"
                                          + "cyl ewcdcmlggqktlxkgndgzyjjlyhqdtnchxwszjydnytcqcb"
                                          + "hztbxwgwbxhmyqsycmqkaqyncs qhysqyshjgjcnxkzycxsbxx"
                                          + "hyylstyxtymgcpmgcccccmztasgqzjlosqylstmqsqdzljqqyp"
                                          + "lcycztcqqpbqjclpkhz yyxxdtddsjcxffllxmlwcjcxtspyxn"
                                          + "dtjsjwxqqjskyylsjhaykxcyydmamdqmlmczncybzkkyflmcsc"
                                          + "lhxrcjjgslnmtjzzygjddzjzk qgjyyxzxxqhheytmdsyyyqlf"
                                          + " zzdywhscyqwdrxqjyazzzdywbjwhyqszywnp  azjbznbyzzy"
                                          + "hnscpjmqcy zpnqtbzjkqqhngccxchbzkddnzhjdrlzlsjljyx"
                                          + "ytbgtcsqmnjpjsrxcfjqhtpzsyjwbzzzlstbwwqsmmfdwjyzct"
                                          + "bwzwqcslqgdhqsqlyzlgyxydcbtzkpj gm pnjkyjynhpwsnsz"
                                          + "zxybyhyzjqjtllcjthgdxxqcbywbwzggqrqzssnpkydznxqxjm"
                                          + "y dstzplthzwxwqtzenqzw ksscsjccgptcslccgllzxczqthn"
                                          + "jgyqznmckcstjskbjygqjpldxrgzyxcxhgdnlzwjjctsbcjxbf"
                                          + "zzpqdhjtywjynlzzpcjdsqjkdxyajyemmjtdljyryynhjbngzj"
                                          + "kmjxltbsllrzylcscnxjllhyllqqqlxymswcxsljmc zlnsdwt"
                                          + "jllggjxkyhbpdkmmscsgxjcsdybxdndqykjjtxdygmzzdzslo "
                                          + "yjsjzdlbtxxxqqjzlbylwsjjyjtdzqqzzzzjlzcdzjhpl qplf"
                                          + "fjzysj zfpfzksyjjhxttdxcysmmzcwbbjshfjxfqhyzfsjybx"
                                          + "pzlhmbxhzxfywdab lktshxkxjjzthgxh jxkzxszzwhwtzzzs"
                                          + "nxqzyawlcwxfxyyhxmyyswqmnlycyspjkhwcqhyljmzxhmcnzh"
                                          + "hxcltjplxyjhdyylttxfszhyxxsjbjyayrmlckd yhlrlllsty"
                                          + "zyyhscszqxkyqfpflk ntljmmtqyzwtlll s rbdmlqjbcc qy"
                                          + "wxfzrzdmcyggzjm  mxyfdxc shxncsyjjmpafyfnhyzxyezy "
                                          + "sdl zztxgfmyyysnbdnlhpfzdcyfssssn zzdgpafbdbzszbsg"
                                          + "cyjlm  z yxqcyxzlckbrbrbzcycjzeeyfgzlyzsfrtkqsxdcm"
                                          + "z  jl xscbykjbbrxllfqwjhyqylpzdxczybdhzrbjhwnjtjxl"
                                          + "kcfssdqyjkzcwjl b  tzlltlqblcqqccdfpphczlyygjdgwcf"
                                          + "czqyyyqyrqzslszfcqnwlhjcjjczkypzzbpdc   jgx gdz  f"
                                          + "gpsysdfwwjzjyxyyjyhwpbygxrylybhkjksftzmmkhtyysyyzp"
                                          + "yqydywmtjjrhl   tw  bjycfnmgjtysyzmsjyjhhqmyrszwtr"
                                          + "tzsskx gqgsptgcznjjcxmxgzt ydjz lsdglhyqgggthszpyj"
                                          + "hhgnygkggmdzylczlxqstgzslllmlcskbljzzsmmytpzsqjcj "
                                          + " zxzzcpshkzsxcdfmwrllqxrfzlysdctmxjthjntnrtzfqyhqg"
                                          + "llg   sjdjj tqjlnyhszxcgjzypfhdjspcczhjjjzjqdyb ss"
                                          + "lyttmqtbhjqnnygjyrqyqmzgcjkpd gmyzhqllsllclmholzgd"
                                          + "yyfzsljc zlylzqjeshnylljxgjxlyjyyyxnbzljsszcqqzjyl"
                                          + "lzldj llzllbnyl hxxccqkyjxxxklkseccqkkkcgyyxywtqoh"
                                          + "thxpyxx hcyeychbbjqcs szs lzylgezwmysx jqqsqyyycmd"
                                          + "zywctjsycjkcddjlbdjjzqysqqxxhqjohdyxgmajpchcpljsmt"
                                          + "xerxjqd pjdbsmsstktssmmtrzszmldj rn sqxqydyyzbdsln"
                                          + "fgpzmdycwfdtmypqwytjzzqjjrjhqbhzpjhnxxyydyhhnmfcpb"
                                          + "zpzzlzfmztzmyftskyjyjzhbzzygh pzcscsjssxfjgdyzyhzc"
                                          + "whcsexfqzywklytmlymqpxxskqjpxzhmhqyjs cjlqwhmybdhy"
                                          + "ylhlglcfytlxcjscpjskphjrtxteylssls yhxscznwtdwjslh"
                                          + "tqdjhgydphcqfzljlzptynlmjllqyshhylqqzypbywrfy js y"
                                          + "p yrhjnqtfwtwrchygmm yyhsmzhngcelqqmtcwcmpxjjfyysx"
                                          + "ztybmstsyjdtjqtlhynpyqzlcxznzmylflwby jgsylymzctdw"
                                          + "gszslmwzwwqzsayysssapxwcmgxhxdzyjgsjhygscyyxhbbzjk"
                                          + "ssmalxycfygmqyjycxjlljgczgqjcczotyxmtthlwtgfzkpzcx"
                                          + "kjycxctjcyh xsgckxzpsjpxhjwpjgsqxxsdmrszzyzwsykyzs"
                                          + "hbcsplwsscjhjlchhylhfhhxjsx lnylsdhzxysxlwzyhcldyh"
                                          + "zmdyspjtqznwqpsswctst zlmssmnyymjqjzwtyydchqlxkwbg"
                                          + "qybkfc jdlzllyylszydwhxpsbcmljscgbhxlqrljxysdwxzsl"
                                          + "df hlslymjljylyjcdrjlfsyjfnllcqyqfjy szlylmstdjcyh"
                                          + "zllnwlxxygyygxxhhzzxczqzfnwpypkpypmlgxgg dxzzkzfbx"
                                          + "xlzptytswhzyxhqhxxxywzyswdmzkxhzphgchj lfjxptzthly"
                                          + "xcrhxshxkjxxzqdcqyl jlkhtxcwhjfwcfpqryqxyqy gpggsc"
                                          + "sxngkchkzxhflxjbyzwtsxxncyjjmwzjqrhfqsyljzgynslgtc"
                                          + "ybyxxwyhhxynsqymlywgyqbbzljlpsytjzhyzwlrorjkczjxxy"
                                          + "xchdyxyxxjddsqfxyltsfxlmtyjmjjyyxltcxqzqhzlyyxzh n"
                                          + "lrhxjcdyhlbrlmrllaxksllljlxxxlycry lccgjcmtlzllyzz"
                                          + "pcw jyzeckzdqyqpcjcyzmbbcydcnltrmfgyqbsygmdqqzmkql"
                                          + "pgtbqcjfkjcxbljmswmdt  ldlppbxcwkcbjczhkphyyhzkzmp"
                                          + "jysylpnyyxdb";

    /** 扩展汉字拼音的首字母 */
    private static final String GBK_4   = "kxxmzjxsttdzxxbzyshjpfxpqbyljqkyzzzwl zgfwyctjxjpy"
                                          + "yspmsmydyshqy zchmjmcagcfbbhplxtyqx djgxdhkxxnbhrm"
                                          + "lnjsltsmrnlxqjyzlsqglbhdcgyqyyhwfjybbyjyjjdpqyapfx"
                                          + "cgjscrssyz lbzjjjlgxzyxyxsqkxbxxgcxpld wetdwwcjmbt"
                                          + "xchxyxxfxllj fwdpzsmylmwytcbcecblgdbqzqfjdjhymcxtx"
                                          + "drmjwrh xcjzylqdyhlsrsywwzjymtllltqcjzbtckzcyqjzqa"
                                          + "lmyhwwdxzxqdllqsgjfjljhjazdjgtkhsstcyjfpszlxzxrwgl"
                                          + "dlzr lzqtgslllllyxxqgdzybphl x bpfd   hy jcc dmzpp"
                                          + "z cyqxldozlwdwyythcqsccrsslfzfp qmbjxlmyfgjb m jwd"
                                          + "n mmjtgbdzlp hsymjyl hdzjcctlcl ljcpddqdsznbgzxxcx"
                                          + "qycbzxzfzfjsnttjyhtcmjxtmxspdsypzgmljtycbmdkycsz z"
                                          + "yfyctgwhkyjxgyclndzscyzssdllqflqllxfdyhxggnywyllsd"
                                          + "lbbjcyjzmlhl xyyytdlllb b bqjzmpclmjpgehbcqax hhhz"
                                          + "chxyhjaxhlphjgpqqzgjjzzgzdqybzhhbwyffqdlzljxjpalxz"
                                          + "daglgwqyxxxfmmsypfmxsyzyshdzkxsmmzzsdnzcfp ltzdnmx"
                                          + "zymzmmxhhczjemxxksthwlsqlzllsjphlgzyhmxxhgzcjmhxtx"
                                          + "fwkmwkdthmfzzydkmsclcmghsxpslcxyxmkxyah jzmcsnxyym"
                                          + "mpmlgxmhlmlqmxtkzqyszjshyzjzybdqzwzqkdjlfmekzjpezs"
                                          + "wjmzyltemznplplbpykkqzkeqlwayyplhhaq jkqclhyxxmlyc"
                                          + "cyskg  lcnszkyzkcqzqljpmzhxlywqlnrydtykwszdxddntqd"
                                          + "fqqmgseltthpwtxxlwydlzyzcqqpllkcc ylbqqczcljslzjxd"
                                          + "dbzqdljxzqjyzqkzljcyqdypp pqykjyrpcbymxkllzllfqpyl"
                                          + "llmsglcyrytmxyzfdzrysyztfmsmcl ywzgxzggsjsgkdtggzl"
                                          + "ldzbzhyyzhzywxyzymsdbzyjgtsmtfxqyjssdgslnndlyzzlrx"
                                          + "trznzxnqfmyzjzykbpnlypblnzz jhtzkgyzzrdznfgxskgjtt"
                                          + "yllgzzbjzklplzylxyxbjfpnjzzxcdxzyxzggrs jksmzjlsjy"
                                          + "wq yhqjxpjzt lsnshrnypzt wchklpszlcyysjylybbwzpdwg"
                                          + "cyxckdzxsgzwwyqyytctdllxwkczkkcclgcqqdzlqcsfqchqhs"
                                          + "fmqzlnbbshzdysjqplzcd cwjkjlpcmz jsqyzyhcpydsdzngq"
                                          + "mbsflnffgfsm q lgqcyybkjsrjhzldcftlljgjhtxzcszztjg"
                                          + "gkyoxblzppgtgyjdhz zzllqfzgqjzczbxbsxpxhyyclwdqjjx"
                                          + "mfdfzhqqmqg yhtycrznqxgpdzcszcljbhbzcyzzppyzzsgyhc"
                                          + "kpzjljnsc sllxb mstldfjmkdjslxlsz p pgjllydszgql l"
                                          + "kyyhzttnt  tzzbsz ztlljtyyll llqyzqlbdzlslyyzyfszs"
                                          + "nhnc   bbwsk rbc zm  gjmzlshtslzbl q xflyljqbzg st"
                                          + "bmzjlxfnb xjztsfjmssnxlkbhsjxtnlzdntljjgzjyjczxygy"
                                          + "hwrwqnztn fjszpzshzjfyrdjfcjzbfzqchzxfxsbzqlzsgyft"
                                          + "zdcszxzjbqmszkjrhyjzckmjkhchgtxkjqalxbxfjtrtylxjhd"
                                          + "tsjx j jjzmzlcqsbtxhqgxtxxhxftsdkfjhzxjfj  zcdlllt"
                                          + "qsqzqwqxswtwgwbccgzllqzbclmqqtzhzxzxljfrmyzflxys x"
                                          + "xjk xrmqdzdmmyxbsqbhgcmwfwtgmxlzpyytgzyccddyzxs g "
                                          + "yjyznbgpzjcqswxcjrtfycgrhztxszzt cbfclsyxzlzqmzlmp"
                                          + " lxzjxslbysmqhxxz rxsqzzzsslyflczjrcrxhhzxq dshjsj"
                                          + "jhqcxjbcynsssrjbqlpxqpymlxzkyxlxcjlcycxxzzlxlll hr"
                                          + "zzdxytyxcxff bpxdgygztcqwyltlswwsgzjmmgtjfsgzyafsm"
                                          + "lpfcwbjcljmzlpjjlmdyyyfbygyzgyzyrqqhxy kxygy fsfsl"
                                          + "nqhcfhccfxblplzyxxxkhhxshjzscxczwhhhplqalpqahxdlgg"
                                          + "gdrndtpyqjjcljzljlhyhyqydhz zczywteyzxhsl jbdgwxpc"
                                          + "  tjckllwkllcsstknzdnqnttlzsszyqkcgbhcrrychfpfyrwq"
                                          + "pxxkdbbbqtzkznpcfxmqkcypzxehzkctcmxxmx nwwxjyhlstm"
                                          + "csqdjcxctcnd p lccjlsblplqcdnndscjdpgwmrzclodansyz"
                                          + "rdwjjdbcxwstszyljpxloclgpcjfzljyl c cnlckxtpzjwcyx"
                                          + "wfzdknjcjlltqcbxnw xbxklylhzlqzllzxwjljjjgcmngjdzx"
                                          + "txcxyxjjxsjtstp ghtxdfptffllxqpk fzflylybqjhzbmddb"
                                          + "cycld tddqlyjjwqllcsjpyyclttjpycmgyxzhsztwqwrfzhjg"
                                          + "azmrhcyy ptdlybyznbbxyxhzddnh msgbwfzzjcyxllrzcyxz"
                                          + "lwjgcggnycpmzqzhfgtcjeaqcpjcs dczdwldfrypysccwbxgz"
                                          + "mzztqscpxxjcjychcjwsnxxwjn mt mcdqdcllwnk zgglcczm"
                                          + "lbqjqdsjzzghqywbzjlttdhhcchflsjyscgc zjbypbpdqkxwy"
                                          + "yflxncwcxbmaykkjwzzzrxy yqjfljphhhytzqmhsgzqwbwjdy"
                                          + "sqzxslzyymyszg x hysyscsyznlqyljxcxtlwdqzpcycyppnx"
                                          + "fyrcmsmslxglgctlxzgz g tc dsllyxmtzalcpxjtjwtcyyjb"
                                          + "lbzlqmylxpghdlssdhbdcsxhamlzpjmcnhjysygchskqmc lwj"
                                          + "xsmocdrlyqzhjmyby lyetfjfrfksyxftwdsxxlysjslyxsnxy"
                                          + "yxhahhjzxwmljcsqlkydztzsxfdxgzjksxybdpwnzwpczczeny"
                                          + "cxqfjykbdmljqq lxslyxxylljdzbsmhpsttqqwlhogyblzzal"
                                          + "xqlzerrqlstmypyxjjxqsjpbryxyjlxyqylthylymlkljt llh"
                                          + "fzwkhljlhlj klj tlqxylmbtxchxcfxlhhhjbyzzkbxsdqc j"
                                          + "zsyhzxfebcqwyyjqtzyqhqqzmwffhfrbntpcjlfzgppxdbbztg"
                                          + " gchmfly xlxpqsywmngqlxjqjtcbhxspxlbyyjddhsjqyjxll"
                                          + "dtkhhbfwdysqrnwldebzwcydljtmxmjsxyrwfymwrxxysztzzt"
                                          + "ymldq xlyq jtscxwlprjwxhyphydnxhgmywytzcs tsdlwdcq"
                                          + "pyclqyjwxwzzmylclmxcmzsqtzpjqblgxjzfljjytjnxmcxs c"
                                          + "dl dyjdqcxsqyclzxzzxmxqrjhzjphfljlmlqnldxzlllfypny"
                                          + "ysxcqqcmjzzhnpzmekmxkyqlxstxxhwdcwdzgyyfpjzdyzjzx "
                                          + "rzjchrtlpyzbsjhxzypbdfgzzrytngxcqy b cckrjjbjerzgy"
                                          + "  xknsjkljsjzljybzsqlbcktylccclpfyadzyqgk tsfc xdk"
                                          + "dyxyfttyh  wtghrynjsbsnyjhkllslydxxwbcjsbbpjzjcjdz"
                                          + "bfxxbrjlaygcsndcdszblpz dwsbxbcllxxlzdjzsjy lyxfff"
                                          + "bhjjxgbygjpmmmpssdzjmtlyzjxswxtyledqpjmygqzjgdblqj"
                                          + "wjqllsdgytqjczcjdzxqgsgjhqxnqlzbxsgzhcxy ljxyxydfq"
                                          + "qjjfxdhctxjyrxysqtjxyebyyssyxjxncyzxfxmsyszxy schs"
                                          + "hxzzzgzcgfjdltynpzgyjyztyqzpbxcbdztzc zyxxyhhsqxsh"
                                          + "dhgqhjhgxwsztmmlhyxgcbtclzkkwjzrclekxtdbcykqqsayxc"
                                          + "jxwwgsbhjyzs  csjkqcxswxfltynytpzc czjqtzwjqdzzzqz"
                                          + "ljjxlsbhpyxxpsxshheztxfptjqyzzxhyaxncfzyyhxgnxmywx"
                                          + "tcspdhhgymxmxqcxtsbcqsjyxxtyyly pclmmszmjzzllcogxz"
                                          + "aajzyhjmzxhdxzsxzdzxleyjjzjbhzmzzzqtzpsxztdsxjjlny"
                                          + "azhhyysrnqdthzhayjyjhdzjzlsw cltbzyecwcycrylcxnhzy"
                                          + "dzydtrxxbzsxqhxjhhlxxlhdlqfdbsxfzzyychtyyjbhecjkgj"
                                          + "fxhzjfxhwhdzfyapnpgnymshk mamnbyjtmxyjcthjbzyfcgty"
                                          + "hwphftwzzezsbzegpbmtskftycmhbllhgpzjxzjgzjyxzsbbqs"
                                          + "czzlzccstpgxmjsftcczjz djxcybzlfcjsyzfgszlybcwzzby"
                                          + "zdzypswyjgxzbdsysxlgzybzfyxxxccxtzlsqyxzjqdcztdxzj"
                                          + "jqcgxtdgscxzsyjjqcc ldqztqchqqjzyezwkjcfypqtynlmkc"
                                          + "qzqzbqnyjddzqzxdpzjcdjstcjnxbcmsjqmjqwwjqnjnlllwqc"
                                          + "qqdzpzydcydzcttf znztqzdtjlzbclltdsxkjzqdpzlzntjxz"
                                          + "bcjltqjldgdbbjqdcjwynzyzcdwllxwlrxntqqczxkjld tdgl"
                                          + " lajjkly kqll dz td ycggjyxdxfrskstqdenqmrkq  hgkd"
                                          + "ldazfkypbggpzrebzzykyqspegjjglkqzzzslysywqzwfqzylz"
                                          + "zlzhwcgkyp qgnpgblplrrjyxcccyyhsbzfybnyytgzxylxczw"
                                          + "h zjzblfflgskhyjzeyjhlplllldzlyczblcybbxbcbpnnzc r"
                                          + " sycgyy qzwtzdxtedcnzzzty hdynyjlxdjyqdjszwlsh lbc"
                                          + "zpyzjyctdyntsyctszyyegdw ycxtscysmgzsccsdslccrqxyy"
                                          + "elsm xztebblyylltqsyrxfkbxsychbjbwkgskhhjh xgnlycd"
                                          + "lfyljgbxqxqqzzplnypxjyqymrbsyyhkxxstmxrczzywxyhymc"
                                          + "l lzhqwqxdbxbzwzmldmyskfmklzcyqyczqxzlyyzmddz ftqp"
                                          + "czcyypzhzllytztzxdtqcy ksccyyazjpcylzyjtfnyyynrs y"
                                          + "lmmnxjsmyb sljqyldzdpqbzzblfndsqkczfywhgqmrdsxycyt"
                                          + "xnq jpyjbfcjdyzfbrxejdgyqbsrmnfyyqpghyjdyzxgr htk "
                                          + "leq zntsmpklbsgbpyszbydjzsstjzytxzphsszsbzczptqfzm"
                                          + "yflypybbjgxzmxxdjmtsyskkbzxhjcelbsmjyjzcxt mljshrz"
                                          + "zslxjqpyzxmkygxxjcljprmyygadyskqs dhrzkqxzyztcghyt"
                                          + "lmljxybsyctbhjhjfcwzsxwwtkzlxqshlyjzjxe mplprcglt "
                                          + "zztlnjcyjgdtclklpllqpjmzbapxyzlkktgdwczzbnzdtdyqzj"
                                          + "yjgmctxltgcszlmlhbglk  njhdxphlfmkyd lgxdtwzfrjejz"
                                          + "tzhydxykshwfzcqshknqqhtzhxmjdjskhxzjzbzzxympagjmst"
                                          + "bxlskyynwrtsqlscbpspsgzwyhtlksssw hzzlyytnxjgmjszs"
                                          + "xfwnlsoztxgxlsmmlbwldszylkqcqctmycfjbslxclzzclxxks"
                                          + "bjqclhjpsqplsxxckslnhpsfqqytxy jzlqldtzqjzdyydjnzp"
                                          + "d cdskjfsljhylzsqzlbtxxdgtqbdyazxdzhzjnhhqbyknxjjq"
                                          + "czmlljzkspldsclbblzkleljlbq ycxjxgcnlcqplzlznjtzlx"
                                          + "yxpxmyzxwyczyhzbtrblxlcczjadjlmmmsssmybhb kkbhrsxx"
                                          + "jmxsdynzpelbbrhwghfchgm  klltsjyycqltskywyyhywxbxq"
                                          + "ywbawykqldq tmtkhqcgdqktgpkxhcpthtwthkshthlxyzyyda"
                                          + "spkyzpceqdltbdssegyjq xcwxssbz dfydlyjcls yzyexcyy"
                                          + "sdwnzajgyhywtjdaxysrltdpsyxfnejdy lxllqzyqqhgjhzyc"
                                          + "shwshczyjxllnxzjjn fxmfpycyawddhdmczlqzhzyztldywll"
                                          + "hymmylmbwwkxydtyldjpyw xjwmllsafdllyflb   bqtzcqlj"
                                          + "tfmbthydcqrddwr qnysnmzbyytbjhp ygtjahg tbstxkbtzb"
                                          + "kldbeqqhqmjdyttxpgbktlgqxjjjcthxqdwjlwrfwqgwqhckry"
                                          + "swgftgygbxsd wdfjxxxjzlpyyypayxhydqkxsaxyxgskqhykf"
                                          + "dddpplcjlhqeewxksyykdbplfjtpkjltcyyhhjttpltzzcdlsh"
                                          + "qkzjqyste eywyyzy xyysttjkllpwmcyhqgxyhcrmbxpllnqt"
                                          + "jhyylfd fxzpsftljxxjbswyysksflxlpplbbblbsfxyzsylff"
                                          + "fscjds tztryysyffsyzszbjtbctsbsdhrtjjbytcxyje xbne"
                                          + "bjdsysykgsjzbxbytfzwgenhhhhzhhtfwgzstbgxklsty mtmb"
                                          + "yxj skzscdyjrcwxzfhmymcxlzndtdh xdjggybfbnbpthfjaa"
                                          + "xwfpxmyphdttcxzzpxrsywzdlybbjd qwqjpzypzjznjpzjlzt"
                                          + " fysbttslmptzrtdxqsjehbzyj dhljsqmlhtxtjecxslzzspk"
                                          + "tlzkqqyfs gywpcpqfhqhytqxzkrsg gsjczlptxcdyyzss qz"
                                          + "slxlzmycbcqbzyxhbsxlzdltcdjtylzjyyzpzylltxjsjxhlbr"
                                          + "ypxqzskswwwygyabbztqktgpyspxbjcmllxztbklgqkq lsktf"
                                          + "xrdkbfpftbbrfeeqgypzsstlbtpszzsjdhlqlzpmsmmsxlqqnk"
                                          + "nbrddnxxdhddjyyyfqgzlxsmjqgxytqlgpbqxcyzy drj gtdj"
                                          + "yhqshtmjsbwplwhlzffny  gxqhpltbqpfbcwqdbygpnztbfzj"
                                          + "gsdctjshxeawzzylltyybwjkxxghlfk djtmsz sqynzggswqs"
                                          + "phtlsskmcl  yszqqxncjdqgzdlfnykljcjllzlmzjn   scht"
                                          + "hxzlzjbbhqzwwycrdhlyqqjbeyfsjxwhsr  wjhwpslmssgztt"
                                          + "ygyqqwr lalhmjtqjcmxqbjjzjxtyzkxbyqxbjxshzssfjlxmx"
                                          + "  fghkzszggylcls rjyhslllmzxelgl xdjtbgyzbpktzhkzj"
                                          + "yqsbctwwqjpqwxhgzgdyfljbyfdjf hsfmbyzhqgfwqsyfyjgp"
                                          + "hzbyyzffwodjrlmftwlbzgycqxcdj ygzyyyyhy xdwegazyhx"
                                          + "jlzythlrmgrxxzcl   ljjtjtbwjybjjbxjjtjteekhwslj lp"
                                          + "sfyzpqqbdlqjjtyyqlyzkdksqj yyqzldqtgjj  js cmraqth"
                                          + "tejmfctyhypkmhycwj cfhyyxwshctxrljhjshccyyyjltktty"
                                          + "tmxgtcjtzaxyoczlylbszyw jytsjyhbyshfjlygjxxtmzyylt"
                                          + "xxypzlxyjzyzyybnhmymdyylblhlsyygqllscxlxhdwkqgyshq"
                                          + "ywljyyhzmsljljxcjjyy cbcpzjmylcqlnjqjlxyjmlzjqlycm"
                                          + "hcfmmfpqqmfxlmcfqmm znfhjgtthkhchydxtmqzymyytyyyzz"
                                          + "dcymzydlfmycqzwzz mabtbcmzzgdfycgcytt fwfdtzqssstx"
                                          + "jhxytsxlywwkxexwznnqzjzjjccchyyxbzxzcyjtllcqxynjyc"
                                          + "yycynzzqyyyewy czdcjyhympwpymlgkdldqqbchjxy       "
                                          + "                                                  "
                                          + "                 sypszsjczc     cqytsjljjt   ";

    /**
     * 获取GBK汉字的拼音的首字母 
     * 若输入是acsii则返回同样的acsii 
     * 若输入是中文字符则返回拼音的首字母 
     * 若输入是中文字符但是该字符不知道如何发音，则返回空字符 
     * 
     * @param hzString  汉字字符串
     * @return          拼音的首字母 
     * @throws UnsupportedEncodingException
     */
    public static String getGBKpy(String hzString) {
        if (hzString == null || hzString.length() == 0)
            return "";

        int firstHalfChineseCode = 0;
        int secondHalfChineseCode = 0;
        char firstByte;
        char secondByte;

        byte gbkBytes[];
        try {
            gbkBytes = hzString.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {

//            log.error("字符串转成GBK出错", e);
            return "";
        }

        int length = gbkBytes.length;

        int byteIndex = 0;
        StringBuffer pyBuffer = new StringBuffer();

        while (byteIndex < length) {
            firstByte = (char) gbkBytes[byteIndex];
            firstHalfChineseCode = firstByte;

            byteIndex = byteIndex + 1;

            //普通的acsii  
            if (firstHalfChineseCode > 0 && firstHalfChineseCode < 129) {
                pyBuffer.append(firstByte);
                continue;
            }

            //GBK字符  
            firstByte = (char) (256 + (int) firstByte);
            secondByte = (char) gbkBytes[byteIndex];

            if (gbkBytes[byteIndex] < 0) {
                secondByte = (char) (256 + (int) gbkBytes[byteIndex]);
            }

            byteIndex = byteIndex + 1;
            if (byteIndex > length)
                break;

            firstHalfChineseCode = firstByte;
            secondHalfChineseCode = secondByte;

            String py = getChinesePY(firstHalfChineseCode, secondHalfChineseCode);
            if (py != null) {
                pyBuffer.append(py);
            }
        }

        return pyBuffer.toString().trim().toUpperCase();
    }

    /**
     * 判断字符串是否由大小写的英文组成
     * 
     * @param str
     * @return
     */
    public static boolean isEnglish(String str) {
        String reg = "^[A-Za-z]+$";

        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    /**
     * 取汉字拼音的首字母
     * @param firstHalfChineseCode
     * @param secondHalfChineseCode
     * @return
     */
    private static String getChinesePY(int firstHalfChineseCode, int secondHalfChineseCode) {
        int pyIndex;

        //查找GB-2312  
        if (firstHalfChineseCode >= 170 && firstHalfChineseCode <= 254
            && secondHalfChineseCode > 160) {
            pyIndex = (firstHalfChineseCode - 176) * 94 + (secondHalfChineseCode - 160);
            return StringUtils.substring(GB_2312, pyIndex - 1, pyIndex);
        }

        //查找GBK_4  
        if (firstHalfChineseCode >= 170 && firstHalfChineseCode <= 254
            && secondHalfChineseCode <= 160) {
            pyIndex = (firstHalfChineseCode - 170) * 97 + (secondHalfChineseCode - 63);
            return StringUtils.substring(GBK_4, pyIndex - 1, pyIndex);
        }

        //查找GBK_3
        if (firstHalfChineseCode <= 160 && firstHalfChineseCode >= 129) {
            pyIndex = (firstHalfChineseCode - 129) * 191 + (secondHalfChineseCode - 63);
            return StringUtils.substring(GBK_3, pyIndex - 1, pyIndex);
        }

        return null;
    }

    /**
     * 
     * 传入汉字字符串，拼接成对应的拼音,返回拼音的集合
     * @param src
     * @return
     */
    public static Set<String> getPinYinSet(String src) {
        Set<String> lstResult = new HashSet<String>();
        char[] t1 = null; //字符串转换成char数组
        t1 = src.toCharArray();

        //①迭代汉字
        for (char ch : t1) {
            String s[] = getPinYin(ch);
            Set<String> lstNew = new HashSet<String>();
            //②迭代每个汉字的拼音数组
            for (String str : s) {
                if (lstResult.size() == 0) {
                    lstNew.add(str);
                } else {
                    for (String ss : lstResult) {
                        ss += str;
                        lstNew.add(ss);
                    }
                }
            }
            lstResult.clear();
            lstResult = lstNew;
        }

        return lstResult;
    }

    /**
     * 传入中文汉字，转换出对应拼音
     * 注：出现同音字，默认选择汉字全拼的第一种读音
     * @param src
     * @return
     */
    public static String getPinYin(String src) {
        char[] t1 = null;
        t1 = src.toCharArray();
        String[] t2 = new String[t1.length];

        // 设置汉字拼音输出的格式
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
        String t4 = "";
        int t0 = t1.length;
        try {
            for (int i = 0; i < t0; i++) {
                // 判断能否为汉字字符
                // System.out.println(t1[i]);
                if (Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);// 将汉字的几种全拼都存到t2数组中
                    t4 += t2[0];// 取出该汉字全拼的第一种读音并连接到字符串t4后
                } else {
                    // 如果不是汉字字符，间接取出字符并连接到字符串t4后
                    t4 += Character.toString(t1[i]);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return t4;
    }

    /**
     * 将单个汉字转换成汉语拼音，考虑到同音字问题，返回字符串数组的形式
     * @param src
     * @return
     */
    public static String[] getPinYin(char src) {
        char[] t1 = { src };
        String[] t2 = new String[t1.length];

        // 设置汉字拼音输出的格式
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);

        // 判断能否为汉字字符
        if (Character.toString(t1[0]).matches("[\\u4E00-\\u9FA5]+")) {
            try {
                // 将汉字的几种全拼都存到t2数组中
                t2 = PinyinHelper.toHanyuPinyinStringArray(t1[0], t3);
                if(t2.length >1 ){ //去重,如 化
                    Set set = new HashSet();
                    Collections.addAll(set, t2);
                    t2 = (String[]) set.toArray(new String[0]);
                }
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            }
        } else {
            // 如果不是汉字字符，则把字符直接放入t2数组中
            t2[0] = String.valueOf(src);
        }
        return t2;
    }

    /**
     * 传入没有多音字的中文汉字，转换出对应拼音
     * 注：如果传入的中文中有任一同音字都会返回字符串信息：false
     * @param src
     * @return
     */
    public static String getNoPolyphone(String src) {
        char[] t1 = null;
        t1 = src.toCharArray();
        String[] t2 = new String[t1.length];

        // 设置汉字拼音输出的格式
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
        String t4 = "";
        int t0 = t1.length;
        try {
            for (int i = 0; i < t0; i++) {
                // 判断能否为汉字字符
                // System.out.println(t1[i]);
                if (Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);// 将汉字的几种全拼都存到t2数组中
                    if (t2.length > 1) {
                        return "false";
                    } else {
                        t4 += t2[0];// 取出该汉字全拼的第一种读音并连接到字符串t4后
                    }
                } else {
                    // 如果不是汉字字符，间接取出字符并连接到字符串t4后
                    t4 += Character.toString(t1[i]);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return t4;
    }

    public static void main(String[] args) {
        getPinYinSet("还是算了吧").stream().forEach(System.out::println);
    }
}
