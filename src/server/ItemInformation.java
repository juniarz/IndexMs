package server;

import java.util.List;
import java.util.Map;

import tools.Triple;
import client.inventory.Equip;

public class ItemInformation {
	public List<Integer> scrollReqs = null, questItems = null, incSkill = null;
	public short slotMax, itemMakeLevel;
	public Equip eq = null;
	public Map<String, Integer> equipStats;
	public double price = 0.0;
	public int itemId, wholePrice, monsterBook, stateChange, meso, questId, totalprob, replaceItem, mob, cardSet, create, flag;
	public String name, desc, msg, replaceMsg, afterImage;
	public byte karmaEnabled;
	public List<StructRewardItem> rewardItems = null;
	public List<Triple<String, String, String>> equipAdditions = null;
	public Map<Integer, Map<String, Integer>> equipIncs = null;
}