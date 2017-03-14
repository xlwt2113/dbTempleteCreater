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
		String stringbuff="{'a':'������߹������H����������������������������������������������V�ˁ��v�����܄������ن��B�懆��a����֊�܍̐ۑ��������l����G���ܜ�G�C�s�a�}���}�v�o�K�i�B�@�L�c�rٌ�t�{�X�P�u���i�q�B�\�a�g�L�I�J�o���°���������������������������������������󃇅\�{�H���������݋F�j��^�ɕ����q�u�бQ�V�U�O�W�I�C�sȀɎ�s�Y�O�t؁�V�Q�@�Bǯ����@�����O���K�c���g�����������n��n���Z�l�a�������°��ð��������������������������������ᮅ����އƇ̈������S�W�����C��S�R�U�j�������`���|���E�����Ѡn���H�l�x�����K�O�b�T΂�\֒֓�E�U�G�O�J����^���q��',"+
			"b':'�Ѱ˰ɰְΰհϰͰŰǰӰ԰Ȱаʰ̰Ұ��������������屁������\�^�z�����ΊB���Q�y�i�p��[�˖��Ȟߠ�X�j�q�F�����T�j�_ƞ���M҆�y�^ڕ�R���Z�T��E�N�������ٰװڰܰذݰ۲����������h�߰ǒ����Ŕ[�����ɪW�q�����]޵�b�oٔ����v��������߰�����������������K�ֈm�Њ����E�����ʕL�D�������t�O�{�O�R�ZΆΌї҃���A�k�l����n�q�k�[���C���X���������������������K�爠�ȉY���L����������ϒ��ʓs�ԗ����g���������R��Ő�K�{�M��rߙ�^�D�u�o�����������������������ٱ����������������������������܃��������E�����~���������ސA�ޖ����h��������ǘ�}˝̙�dЈ�����f�Jم�E�t���d�s���h�b��U�R�d�_��`����������������������������������������������������Ղp���˂�F�\�h�Ո������ʑv�d�K�{�ȗG�f�����������ͪN�D�i�w���F�t�L�pƅ�����i��ˍ͓���o�R�|ؐ���K݅�f�m�^�C�E�c�l������������������傖�ω��M�ٓ��L��y�`�ǟ��Ī����n�Sݙ�G�Q�M�ı��±��Ű�����ԁ�ٺ������爩��ܡ�l�R��ƽ�꒲�s�԰�m�Ԭa�e�n�p�l�X���E�M�J�A�a�G�a잱ȱʱձǱ̱رܱƱϱ۱˱ɱڱͱұױٱαбӱֱݱѱ�������޵ݩ����ذ�����������������������������������������������Ӂط��������񆞆�f���㸴���`�����ǋ�����Ŏ��ϏY�����P�W���Ϸ��������a�������ėa��������ța�����������Ϊ������n�w���H�R�w�n�zƳ���K���P�t�u�v�`�z�������a�o�V��T�X���Y�Z�^Ƣ�M�bƃ�]ɜ�Y��͚ΓЋ���K�g�v�vם�P؄�C�M�S�F���P���~ۋ�K�Lߛ�����t�G�C�q�@�E���[�\�]�s�\������@�S�{��{�E�����x���I�m�s���p�S���z���G���Ա߱�������ޱ����������������������������ԉ��O�̐ƒ\�ՓO�c�M�נ��p���K�V�H𡹏�e�D�����XŌ�q�xҌ�S׃�P�H�g�k�l�p�q߄߅ߛ��Q�r��Y�b�c�u�@�Ա�������������������ڂl�G����w�Ύ��ғ��˙~�W��Ư�d��Ϡg�Y���gà�r�A���Eˑ�FՕրٙ�l���S�s�R�[�j�k�l�n��Q�T�B���Ա�������e�񏕏ְǰΰƓ�Ʋ�Īm�T�ط�ƃ�a��̋�r�h�X�M����������������������������������݃��P�Ú�����亞I�M�l���p�W�_Ĝ̞ϙӟ�h�e�fٚ�Sߓ�\�E�l�Ƶ�x���W�����������������������������K����v���������}ƽ�Վ�T�mƴ��\�m�ʖޖ◀������]�ެV�n�R�S�}�A���p�l����u�s�@�M��u��v�h��@�S��V�m�ڲ���������������������������������ز����������ǲ�������������ެ������뢁����N�`�k��ļ�\�h�������ÌX�󎓏������İΒ���ߨ�����K�_�q�ؚ��Û_�������ٟ������ݠ���t�������h�l�װٰ��C�j�B���R�z�q�����~���\�����J�`Ň���tƅƞ��ȕ���N�X޵���Y�oГ�B�J�T�U�q�y�L�m�����@�R��K�\�c�n�}�D�G�o�A�C�F�g���m�p�E�N�Q�����P���������������������������������������߲�уW�υą����猠�m��E���p�ԒÒѓ�򖿚h�i�ߪ����r�^�[ɞ���a�c�Yݕ�K�^�b☸�����J�X���G�L�Q',"+
			"c':'����������͔c�U�~�g���n�Ų˲ɲĲƲò²Ȳǲ̲ʂ��Ɔ��Ɗ�u��A��H���Z�n�uؔ�P�ϲв��βҲѲͲ���������ӂ����������ЇA�k�ԋۋ�ߑK�L�M���⓷�����\���ӠN�|�_�d�L�]�Q�T֍ۊ�D�{���Y�����o�زֲײղ������}����M�ș��P�����[���œ�n�i�@ى��I�]�ݲٲܲ۲����������������F���ٕ����H�Hܳɘ�G�_�����[�A�x�����߲�����ȃԅ��x���������ő�������դ�y�����Z�k���u���R�Y�mɃ���v�ֲ��久������ߗq�N���_���d�~�������ɮ���Ӎ�I�}����򚳀���K�e�u�����������ɲ�������������������ꁚ�����x�������g�����K�Q�����ݽӓc��б�˗^⪮��g�p�P�aő�O���NݱіӐԈԌۂ�O���\�d���x�l������������٭���в憶�ВK����}��Ə�OϊЃӐ�O�b���������������������������������������������������夁P���׃]�d�{�����Äi�}�����g�冮�·c���ψF���ʋ���ո�ݍ��f�ʑ��ϑԵ��������۔v��C�{���^�ٚ�մ�������I�u���e���������a�b���v�U�o�g�C�A�W�p�s��È�Bɻ�r�M�sπП���g�R�b�cҗ�S�~֝׀׋׏��ۅ�{�����p�@�a�i�P��K��U�]�𒳤������������������������������������������������潁��t�ȃY���������L�����j���ЏS�����������ʜC���`�d�����D�^��Û�c�q�O�^�m�k���_�L��L�M��O�o�K�l���������������������˴³��������ˁ���������Z�l�z�������ș��ΝR���q�����|�J�}�B�K�b�U������V�e�N�a֚���}ڈ�C�}�n���{�������������߳������e�����q�ö���bլ���ؓF���ݳ�؟E�L�c���u�����s�p͒�a֐܇ފ�J�óƳ����������³ĳȳ�������������������衂E�ს�����\��m�}�����גדZ������F����������տ�c�G���M�����_���~�Q�D�b������Ɛ�_�k�c�l�I�r�HՀ�R�n֍׏�oٕ�fڒڙ�{ܕލ�p�\��J�����m���Y�Z�ӳɳ�ʢ�ųƳǳ̳ʳϳͳѳҳγȳ�����������������������ة\���D�t���\��Ǻ�܈���S���k�w�ӎ񏻏��琑�r�~�������ޓ������Γճ��b���������̘����f�r�雄��䥛ƛ՜Q�˝��j�s�������A�b�������������Q�����V�f���B�wÔ�^�B�|�c�\�W�X���k���J۫����j�d�p�K�p�l�d���G�r���h�ӳԳ߳ٳس�ճ�ݳֳܳ�޳ڳ۳��������ܯ����������������������߳�������с́��s���ȅh�q���Յ����Хϲ�ˇV�[�i���k���L���w�эI�����Ӑp�u�J�d�y���Ļ�x���ϒ������������~�o���ܘ��I�^�l�n�����n�Λ��F���͜��k�ßU����E�b�v���V���l�o���Ƹ��M�x�}���Y�����@�B�]�L���n�qÒ�S�β��K��Ǡ�N�h���y�D�E�l�WԠ�B�v�s֖�P�d�m�pچڗ�F�L���y�Bދތ�W�g�o�t߆�M�r��~�x����S�����Y��P���T�|�A�`�������u�J�[�X�cۭ���س����ӿ�����������������׆����Ì����בo�_�����_�ҝr���򠂫��\�ѵr�N���Z�����u�x�n�~ی۠ͪ�|꙳��������������ٱ����ŁE�G����������樋B����ΐJ��Ť����o�F�����䗹������b���ߠ⮇���{�T�y���N�I���[�I�WǓ�g�nԗ�a�pׇ׉��מ�S�P�c�O�b�h�~�oť�l�{�\���ֳ������������������������������������ۻ���ء���������˂m�⃁���I���b�ǋ���ЏN�A�Ò}�����������ƘZ���[�s���������bͿ�����ˬG�`�l��Aף���a�e�X�I���^�aĕ�cǈ���Z�eɍ�xˠ̎�{�E���c�|�xՑ�Tڰ���X�a،�n�H�U�u�X�s�I�z�z�r�R���i�s������H�_����������ߚI�D�u�����������������������݂��b�����@���ĚN���k�[���U�@�x�x�F�i�w��ݎ�A���E��������������ց��}���V�k�y�����l��ѐ�ꓜ�S�H�r�w���������}���RĀ�����[�J����������׵����������������Dǔ�]�N�m�q�D�s������������������ݻ�Ȃ��b�@�a�ÕI���Ԗ~�J���Ꙛ��̜����_�Ƭt�Q�������aË�x�X�N�o�Oɔك�w�bݐ���c�T����j�������������꡴مɇǊŊƋC�S�F�������f�����̴����ؾY�b��׺�J�����}���T����z�u���O�Qȩ�r�Y��q���p�w�δ˴ʴɴȴƴŴǴ̴��ŴôͲ����������فՁ肽�p�ޅ�˾�膝���􊜋��ύՎ����c������ܖ曁�̝ОB�u�y�N�r�Q�ҽa�W�h��ƘƝ��Ǆ�W�F�j��y�Έυ�~�n�a�e��ڝ�e�i�o���@�d�q�J�y�\�]����ӴԴдҴϴ�������������̅����Q�����ď�S�����F�m�����B���ژ�ߝY�^�|�������t�РQ���B�j�����b�t���k�v���S��Ɖǈɐ�[�q�Z�pց�z�{�W�^�����������圐�u����޴˒��Ȥڅݏ�ִ״ش�����������������҅a�m�ŊƋ{�I���ݒۯ|�����U�q�Ð�I�P�c�K���uȤڂڅ�U�c�n�q�A�e������������y�ܴڴ���������ߥ�V�m�e�x��꿙��皖�U���䷉�U�Z�e�xǈ�{�f�g艴ߴ�ݴ�޴�˥�������������t����y�������썌������N�����y���n�ժ��X�Q�����W�����\�\�������yÜăċě�P�~�n�J���x�����������Y���������������v�Zߗ������������������������v�z��Վ������̠I�����W�s�u�c�x�P̑�PՋ���g�H�u���A�i�S�e�q�z��',"+
			"d':'���������������������������������A����}�����������ҙ\�Κ��͞ؠ[�}���o�K���J�e���E�Q�Rσ�u�z�Q�Jޅއ���Q�_�]�p�N�^�����������������������������߾�����߰�����ܤ�����N���������K���Ζ�馶������y�x�y���H�Dŕ�O�f�}�E�y�rڱ�J�Fې܍ܖ�D�a���f�r���O�^�W�j�~���\�l�n������������������������������ʯ�������������������ࢁS�������d�{��Ƚ�[��S�g��m�����·d�n�~��̳���l���[���������򐴐ב������b�ړۓ����ܚK���X��տ̶夝ڟ���m���X�D�D�Q���N�V�������W�n�E��đ͞��π�yэ�R�bҗ���gӔղ�Qٜ֝٠��ې�l���G�]�K�F�Z���d���}�^�l�r���鵱����������������������}���W���������P���ɐՓ����n��띋�Ǟ��̠C�����T�c�G�����U�^�X�D�Y����řʎ�o�}�d�[וځ�T�K�W��h���������������������������������߶ٱ�������������p�����u���W����㰒ғv�F�]�͘��|����Ν��c���I���\�R�|�r���R�s�K�g�@�m�p�n܄�~�����I�Z����B���ֵĵصõµ�ｇN�Ԑz�����������u��ÆO�g�O�Y�ȵƵ˵ǳεɵʵ�������������\�����~�ș����O�~Ř�R�O����Q�صڵ׵͵еֵε۵ݵյܵ޵̵ĵ���ѵϵҵԵ���ۡ��ڮ��������ص����ݶ���������Ɓ��d���C���ׅ}���v����ഇ��h�k�������y�����D�X�ގR�f�t�w�~���K�d�F����W�����A輖m���\�b�N�����v��b���Z���S�F���Y�����E�a�e�s��Լ�J���V���l�mݯǅǜɉ�H�L�O�yˋ�h�[�EД�]�h�g՜�B�p�d�h�q�y��ۇۗ�B��ޞ���f�m�r�K�d�~�C�s����{�L�E�}���V����W�M�ǵ������ߵ�������������������������ׂم������|���H���L�юo�p���锄�����Ϙ�����Aմ�ќ��լU�t���d���H�_�s��͟�aۆ���F����c��ؼ�������������������������٬�õ��a�P���ŏt�����F���������@�h���f�H���L�u�y�мg�r�I��ݯɉ�y�@�q��ӎ�A�{���w�����U����y�H��c�S��M����M���B�J�@�m�b�����������������ܦ���������������A�O���U���ގ��L�g�䒔���i�薻�G�����ۛu��h�͠������A�B�y���Ͻx���W�]���\Ŏ�|�H�Hс���B�eՙ�g�@�L��ې�W����F�P�C�]�l�����ڇö����������������������������������व�c��������͡��Ʈk�G�O���J�b���Y�Bӆ��b�V��w��r�}𗶪���G�A�M���������������������������������˂��P���ӄ�ĉ������ٍd�����㒜�_�k�|ͩ����򜧞����J𮸕Ͳ�c���p�L�{Ǉʐ�X�hԘՉޓͪ���L�[�C���H��⺶������������������������c�J�ÄE�r�ʆtͶ�Ԗ��u�X�Úћ��^�f�]Öŏ�W�x���K�H���^�W���k�L�h�L��Y�Z�^�`�a���ȶ��ɶ¶��ǶƶĶöŶ������ʶ��������������ܶ���E�����X�������T����լ���������̘��ҙ�������^���٪��o�{���G���]�ø��V���T�}Ζ�C�t�e�l�GԌ�i�xט���Kـ�G�`�L��N�o���A�^��|�b�~��D�y�t�ζ̶϶˶Ͷ�������Ǆ��F�e��������V�@�f���Z�aȘу���Y��H�ԶӶѶҶ�������������͉[��Z�����ő��������A�S�w�}�y�q�q��Ą�c�d�B�m׷�q�J��T�m�������X���c�ֶٶ׶նض۶ܶڶݲ����������������������ݎݏ������Ǔ�J�������Ԫ��]�R�]�P���v�H�Oޚ�q�g���D����������޶��Զȶ�������������綁��������҄A�m�������Ƈ������ʉ�����Z�b�s���������������������������\�Ӗm���ޖ��Ö�颗ٙE�Ǜk���ɯk���EƖ�E�q�B�y�r�F�G�T�o܀������T�N�I�����w�y���D���W�j�D�y�z�c',"+
			"e':'��Ŷ������ﰢ������������������������ݭ��������ج�����ǁ���α��٨���΃^�i���\�v�Ņن@�H�Ɔs���������f�ه��눺�׈�񊊊������j�ߌ�S�k���֎��������t�~���A�����ĚG�d���x���ᜊ�M���c�u�x���S�X�b�j�r�Ҵd�J���Jʂ�F�L�Y�FӞԛ�M�@�F׆�`ܗ�Qݑވ�]�{���e�~��y�p��i�q���O�P�~��I�_�Fت���t���{���E�Z�[���L�|���������ŊC�W�������E���s�����������������������٦����������n�����茩���X�p�r�k���ޚ����x妠����Y�x�sĞ�H�n�^�LԠ�@�Eڍ�[݉�W߃�s�z��X�D����b��',"+
			"f':'����������������������늑�U���β��ܖ����o�����m�V�k�l�X���P�Uƞʆ�t�H���e�w�z�y�󌷴�������������������������������������ެ����ᦁ����F�G��K�h���i��ˋ̋я鑌�B�Ք���i������������t�J�~������[���F�\�B�D���O�o�u����ć�t�w�x�X�ϛ���Tӌ؜܏�G�N�x�C�x�c�x���Y�����ŷ����ķ����÷·����������������Ђ��Έ��ڔ�P�X�\�P�E�����f�}�p���K�Lړ�[�p���������h�J�ǷɷʷѷηϷ˷ͷзƷ̷�������������������������������n�|�����O���Ў��U�����h���ՖF�{�ɗ���J���d�O��U�i�X�C����A�E�p��^�c�dƅ���Qʄʆʈ�N�n���p�q�U�u�M���]�q�w�y�[�I�W�a��E�����ַݷҷ۷طܷ߷׷޷�ӷٷԷշ�������������f���燊�b�e�����^�}�����ˏk�����Ք��S�B�D���r���J�\��ǟ����ܪ��βb�W�l�j�S���i�����RďȆ�kʈ�_�`�vӟ�k�r�S���m�M�p��M�V��C���i���B�O���a�X�R�v�������������������ٺۺ�����􁧂��t�K�N�O������~���o���������Q���l�h���͜t�����m���Пu���ԠȪh�S�g���`�^�A���p�T�}ł�l�X�t��҃�S�N�Sو�R�����h��Q�b�L�p�T�L�P�i�i���K҅�������u����󾲻���v�伀�����]�[��]�����������������������������������򸸷����󸳸���������������������������������и����������������������������������������������������������ݳ���������߻ۮܽ���겻�T�����Y�a�������_��߼��b���`�}������m����D�c�ˋ�嵌��@�T�����}�܏��ͻ��N�ʒh�������ꖎ�������󗭗ӗژ_�L�^�Л����ߝ��ޟJ�r�c�s�M�o�i�t�w�}���G�Q�y�O���J�c�A�[�r�������J�����E�n�������`�b�J���i�wŀ�]ƅ�C�X���Q�iȃɒʍ̒�b�k�|͗�l�uГЕ�}ч�L�i҄Ӈ�c�D�fؓ�M�xَ���~�P�oݕݗ��ߏߑ�G�M�~�K�U��a�R�V�����������h�v��\�O�T���f�H�V�v�v�D�I�W�q�f�����A�F',"+
			"g':'����и¿�����٤��������ȇQ�V܈��m�p�h�øĸǸŸƽ�����ؤ�����_������Ȋ��Y�㓩�|�q�˘������[�y���o���i�w���Dȑ�wԓ�d�W�^�@�}�Y�u���d�B���ϸɸиҸ͸ʸθ̸˸Ӹ�����������ߦ�������������qǬ�x���΂����Q��錼�����֏�I�����U�o�g���N���l���\�q���Y���C�m�T�����C�Q�H�r�l�P�J�C�M�s�|�F�D��_�v�x�h���N�ոָٸ۸׸ڸܸԸؿ���������鄂�Ո������ޑ߿��◞���ϟ���� �I�M�L�V�������G��s�l�@���߸����ݸ����۬ھ���غ�����޻�ƅ̾̉��z�������R�����Ɯ��������w�G�����J���X�l�c�o�Vǐ��˛�a���z�p�{�k�����������������￩���������ٺϸ��������ܪ���������ت�����������ށ������ɅÅφ��S�����͑��M���R���q�����Ɯ�𝻠�����n�������w�v�Iė�Zƌ���w�x�\�kԆ�Y�gݑ�P��t�x�d�k押����l�w�xբ��R�k�u�M�Z�����i�s�����m�w���B��������ݢب�ޓ^�j����������������������箁����j��y�a�������ԛʟ��uӲ�b�c�����K�}�c�@�s���D�Q�i�i�����f�ֆ񹤹������������������������������������@�C���ۆy�߉b�m���Ð�E���r�ܖ�|�˴b�T�k�t�������yؕ�C�M���p�\�U���������������������������������������ڸ���������׃����^��x�򊥒]�Гk���t�ϟ��v�����T�U�Vƙ�mНѐ�M�_ԍ�m�xُ�@�h�^�g�x�L�z�Y�ŹɹĹȹʹ¹��ù˹̹͹����ǹ����Ƽֹ��������������������������ڬ����������������ł�l�󄽸��߆f�g���؆�؉������g���H��M�_�ݖ����b���O���}�����J�k���ް��W���h�����Y�����H�M�S�B�l��Ù�V��Ƃ�u�[�v�M���b�Z�L�Mݞ�E���d���k����Y�A���]�X���ҹιϹѹй����������ڴ�ք������F�J���q㷒�O�����\���G�L��Ƃԟՠ�o�|��T��N�W�m�����ֹչ��Ⅸ�������s���ʹy�L�عܹٹ۹ݹ߹޹�ڹ��������ݸ���������āO���j�������T�ד��ӹ���������ٛ����S�����g���p���H���e���A�`�]���o���X�Fҋ�Q�^؞�I݄�k�]���r�v�K�P�q�^���b�}�A�J�X�����������ׁ�U�Z�����ڏV�������єU�ᙤ�����ꞻ�Ӟ՞֟D�E���q�Q�Sƚڇ�_ނ��U��������������������Ȳ����������������������wα��΃^�����Q�хT���ۈ����ދ������@�Q�`����@�i���ʔ����Q�_Φ�˗θŘ��������u���ƙ͚w�����ݜ��ĝ����Y���I�Z�n�z�u���W���K�l�}�L���vĄƗ�}�l͊Κ�j�O�^Ҏ���mԎ�d�F�bڑ��܉ߞ�F�|�{���v�W�o�h�k�q�Z�i�W�_�`���������������ح������윆�L�g�e���O�i��ɀ�FЖ�r֏݁�K��P����������������������������������������廮�F�J�����H��������͈剁���������I�ꓝ���ۗ똡������u�X�㯆���l�@�[�B�N�sǑ�b��΁�X���x�{�^�u���J��R�',"+
			"h':'����Ϻ���R�źǊU�o����r�x�������Ⱥ��������������܁���Ԇ㇯�h�����Q�n�w߀�@�V�u���W������فa�������������������������������������������������������������΂��˄T���{��c�i�������΋��南��Ƕ��ВI��꺕~���U�c�ߘo���a�N��țۛ���̲�h�����Ȟ��ߠ��R�H�ʮ]�y�����b�`�Q�t�G�k�H͔�K�LΑ֛�E�J܎���F�j�I�d�w���\�R�\�n�y�n��M�h�u��H�U�A�_�A�N�[���ﺽ������������񈜊s�����쿻�Ǹ��V�W���f�aؘކߒ���@�úźƺ����º����ĺѸ�������������޶�ۃ���S���_�s椐h���a���������ؗ��B��������|���ް�������غ�m�h�D�G�|�z�Vƒ�A�^�r̖̗ϖՒ�q�z�e������ͺȺϺӺ̺˺κǺɺغպֺк׺ʺѺҺ����������������ڭ�����ú��J���Y������྇m���������y�P���Ԓu���Ҕ������B��Ǣ�f�z�ʜ����Z�������_�e��C�F���A�B��E�����K�t���v�U���G���M�r޽��ЫΘϚЎ҇�X�ZԆ�y؀�R�`ݠ��Ͻ�A�F�Y�u�H���a���e�f�g�H�M��g��]�����]���K�Q�S���i���L�[�������ˋϝ��\�ٺܺݺ޺ہ��燌������ԋ앺��ߺ��������޿�䆑�����a���M���B�tÆ���U���C�a�Y�b����������ڧ��ޮ��ް��ݦ���������ƅˆM�y�߇����k���f��c����E�Е{���K�ě͸ۜ|���~�������p�Y�a���{���f�s�C���t���������������o�|�yȇȈӏ�h�A�D�Fܟݓ�Z�v�p�U�f��b�{������\����Z������������ܩ�����������˅鈋������T�@�F�J�Dȉԍڸ�_�C�j��A�f�`�\�c���J��������������������������������Ϸ�˺ͺ������������������������������������������������΁[�s��I�O���؆ۇF�P���~�։؊����|���������ďu�H�m��������_�����N�O�U�����������X����R�Z�~�����W�X���G�C���W�ڭ��@�C���}���n���`���S�T�e����Ċ�n�S�U«�̿��~�L�S�d�̏̕Ι�k�{�S֗�o���mܠ����n�t��L���a���i��_��g�b�E���U�{�s���U���]���C�K�X�I���������������������������٨������ۇW�����N�O����ѧ�W���ғ�����Ж��ɘ��噊䫝��Ҫ��L�������h���@�A�E�B�p�_�A�J�y�f�sΔ�iԒ�jՖՠ֜�f�{�D�E�n�k�f���q�����X�������������ׁ݆Fఇ]�������đ��љƞx�j�x�|ёќ���������������û»������������ߧ����ۨ���ۼ���������但����ȇ�����J�A�~�`���w�ё��דQԮ�k�h�����Z�g���o���ȝ׹��🨠�B�~襭h������ѣ�����`�o�]�b�����Q�v�K���B�P�VːΌ�Sג�h�o�}؎�k�S߀��h�D�G�I�X�a�f�q�O���Z�d�b�J�X�q�ٻƻŻλĻɻ˻ʻѻ̻Ȼǻлͻ�������������������諂��Ɔň��E���N�h�r���x�U��N�s�͖M�R�n���R����ꟺ��p���������k�mŊâãȏ�Yԅ�W�eڇ��w�u�B��c�b�m�U���S�ػ�һ�ӻ�Իٻڻݻ޻ջֻ�ۻ߻׻仲�ܻ�������ޥ����������ڶ������������݃a���R���G�j�v�����߇�H�鉙�Ċ�^�@���Ꮉ�@�h�j�����ڐo�{�}�Β��]�֕�������ۗ򘞙B�b�m�u�������i�x�������󝌝ҝ�`���@�C�F�����S�Z���q�_�g�`�����N�~���x�_�}�D�L�����E�P�U�L�]�C�D˙�l�s���Y�z͠ΚЄы�^ԐԜ�d�M�M�e�f�w�Y�V�x�{Υޒ�D�`��i��T�_�D�u�W�f�}Τ�_��d�t���H�I�����������������ڻ�[�����G�����ǐ�������]���e�p�y���������������[�k�q�d�E�J�i�r�������ȝ�M�o՟�F���Q�a�@�������ͻ���������������޽߫�����ⷁ�i����������؇��Ɋ_���璻��n�N����뙊�[���t��C�m��Z�@�\�n���������������W�h�N�s�Gş�u�^Ւ�f�m؛Խ�o�^ߘ��x�Z�X�b�o�[�d�r��',"+
			"i':'�U',";
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
		if(i<1024*1024){//С��1Mʱֱ����Ϊ�ڴ��е�byte�����������
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int len;
			while((len=in.read(buff, 0, Math.min(i, buff.length)))!=-1&&i>0){
				out.write(buff,0,len);
				i-=len;
			}
			fin = new ByteArrayInputStream(out.toByteArray());
		}
		else{//����1M���ļ�������ʱ�ļ��Ļ���
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