package net.aetherteam.aether;

import java.util.Random;

public class AetherNameGen
{
    public static String[] valkNamePrefix = {"Har", "Her", "Gon", "Sko", "Hil"};
    public static String[] valkNameMiddix = {"fjo", "ska", "bri", "", ""};
    public static String[] valkNameSuffix = {"tur", "pul", "dul", "gul", "or"};

    public static String[] name1 = {"毁灭","狂暴","毒辣","邪恶","诡异","超能","狂拽","浪气","黑暗","暗黑","狂怒","贪婪","傲慢","虚荣","饕餮","嫉妒","色欲","暴怒","懒惰","黑化","无头骑士","三无","基佬","百合","胸大无脑的","金刚不坏的","独霸天下的","目中无人的","骨骼惊奇的","武学奇才","节操全失的","灭神屠魔的","魔中之魔","醉鬼","游手好闲的","神机妙算的","唯我独尊的","聪明的","可爱的","活泼的","漂亮的","阴狠毒辣的","狡诈阴险的","道貌岸然的","妖魔化身","酷拽狂霸的","魔中之皇","毁天灭地的","开山捭石的","美如冠玉的","雪肤花颜的","国色天香的","闭月羞花的","高手高手高高手","地上无敌的","残暴无情的","灭绝人寰的","人间劫难","东方不败的","独闯天涯的","无情无义的","冷面杀神","冲天黑煞的","屠杀者","恨天者","补天无力的","无尽轮回的","绝境之","普通的","一般的","平凡的","没什么出奇的","佛面魔心的","上天入地的","色中狂魔","人间失格的","食节操魔","狂拽炫酷的","独孤求败的","梦中恶魔","猩猩队长","遵纪守法的","见义勇为的","拾金不昧的","肌肉化身的","魔神之子", "富甲一方的", "业务繁忙的", "膝盖中箭的", "无辜躺枪的", "科学教徒", "打假卫士", "极品", "力大无穷", "嚣张的", "人见人爱","拉风的","Too young,too naive的","查水表的","拆迁办的","有关部门负责人","死神代言人","专家","成功人士","英灵附体","关二上身","屠狗者","异形","天外飞仙","天外来客","成熟体","究极进化的","幼生体"};

    public static String[] name2 = {"pa001023","sun","waidely","zestybaby","craft","zesty","crafteverywhere","Azanor","RichardG","zlainsama","pahimar","Alblaka","鸡排","脆饼","kingbdogz","indeed","fanhua","HyperX","Player_Kuso","木是伊","direwolf20","12dora","郝先生","重生是希望","景景","升天的猫","苍生","comeheres","szszss","希特勒","元首","和尚","籽岷","stevefan","dawn","banner","闪耀之灵","sy","SF","wzp","CaptainSparklez","邦莫","米二","zero","ztcjohn","某猫","蓝色铁卷门","大作之月","disco","黑桐谷歌","敖厂长","CovertJaguar","SirSengir","eloraam","大刘","当风过时","小木","kitune","Lawrence","yuxuanchiadm","Qrox","johnbanq","爱丽丝","hendyzone","jcku","tesso","华子","随风澈","WJMZ8MR","天命小猪","卧室小强","矮人","Littlebear","powercrystals","鳄鱼软趴趴","Forever_小树","719823597","outsidero","miguo","DJXGAME"};

    public static String[] name3 = {"pa001023","sun","waidely","zestybaby","craft","zesty","crafteverywhere","Azanor","RichardG","zlainsama","pahimar","Alblaka","鸡排","脆饼","kingbdogz","indeed","fanhua","HyperX","Player_Kuso","木是伊","direwolf20","12dora","郝先生","重生是希望","景景","升天的猫","苍生","comeheres","szszss","希特勒","元首","和尚","籽岷","stevefan","dawn","banner","闪耀之灵","sy","SF","wzp","CaptainSparklez","邦莫","米二","zero","ztcjohn","某猫","蓝色铁卷门","大作之月","disco","黑桐谷歌","敖厂长","CovertJaguar","SirSengir","eloraam","大刘","当风过时","小木","kitune","Lawrence","yuxuanchiadm","Qrox","johnbanq","爱丽丝","hendyzone","jcku","tesso","华子","随风澈","WJMZ8MR","天命小猪","卧室小强","矮人","Littlebear","powercrystals","鳄鱼软趴趴","Forever_小树","719823597","outsidero","miguo","DJXGAME"};


    public static String gen()
    {
        Random rand = new Random();
        String name = name1[rand.nextInt(name1.length)];

        int middle = 2 + rand.nextInt(2);
        for (int i = 0; i < middle; i++)
        {
            name = name + name2[rand.nextInt(name2.length)];
        }
        return name + name3[rand.nextInt(name3.length)];
    }

    public static String valkGen()
    {
        Random rand = new Random();
        String result = "";
        result = result + valkNamePrefix[rand.nextInt(valkNamePrefix.length)];
        result = result + valkNameMiddix[rand.nextInt(valkNameMiddix.length)];
        result = result + valkNameSuffix[rand.nextInt(valkNameSuffix.length)];
        return result;
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.AetherNameGen
 * JD-Core Version:    0.6.2
 */
