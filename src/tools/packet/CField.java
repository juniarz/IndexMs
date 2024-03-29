package tools.packet;

import handling.SendPacketOpcode;
import handling.channel.handler.PlayerInteractionHandler;
import handling.world.World;
import handling.world.guild.MapleGuild;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import server.MapleDueyActions;
import server.MapleShop;
import server.MapleTrade;
import server.Randomizer;
import server.events.MapleSnowball;
import server.life.MapleNPC;
import server.maps.MapleDragon;
import server.maps.MapleMap;
import server.maps.MapleMapItem;
import server.maps.MapleMist;
import server.maps.MapleNodes;
import server.maps.MapleReactor;
import server.maps.MapleSummon;
import server.maps.MechDoor;
import server.movement.LifeMovementFragment;
import server.quest.MapleQuest;
import tools.AttackPair;
import tools.HexTool;
import tools.Pair;
import tools.Triple;
import tools.data.MaplePacketLittleEndianWriter;
import client.MapleBuffStat;
import client.MapleCharacter;
import client.MapleClient;
import client.MapleKeyLayout;
import client.MapleQuestStatus;
import client.MonsterFamiliar;
import client.Skill;
import client.SkillMacro;
import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.MapleAndroid;
import client.inventory.MapleInventoryType;
import client.inventory.MapleRing;
import constants.GameConstants;

public class CField {
	public static int DEFAULT_BUFFMASK = 0;
	public static byte[] NEXON_IP = { 8, 31, 98, 53 };

	public static byte[] getPacketFromHexString(String hex) {
		return HexTool.getByteArrayFromHexString(hex);
	}

	public static byte[] getServerIP(MapleClient c, int port, int clientId) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.SERVER_IP.getValue());
		mplew.writeShort(0);

		mplew.write(NEXON_IP);
		mplew.writeShort(port);
		mplew.writeInt(clientId);
		mplew.writeInt(0);
		mplew.writeShort(0);
		mplew.writeInt(-1);

		return mplew.getPacket();
	}

	public static byte[] getChannelChange(MapleClient c, int port) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.CHANGE_CHANNEL.getValue());
		mplew.write(1);

		mplew.write(NEXON_IP);
		mplew.writeShort(port);
		mplew.write(0);

		return mplew.getPacket();
	}

	public static byte[] getPVPType(int type, List<Pair<Integer, String>> players1, int team, boolean enabled, int lvl) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.PVP_TYPE.getValue());
		mplew.write(type);
		mplew.write(lvl);
		mplew.write(enabled ? 1 : 0);
		mplew.write(0);
		if (type > 0) {
			mplew.write(team);
			mplew.writeInt(players1.size());
			for (Pair<Integer, String> pl : players1) {
				mplew.writeInt(pl.left.intValue());
				mplew.writeMapleAsciiString(pl.right);
				mplew.writeShort(2660);
			}
		}

		return mplew.getPacket();
	}

	public static byte[] getPVPTransform(int type) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.PVP_TRANSFORM.getValue());
		mplew.write(type);

		return mplew.getPacket();
	}

	public static byte[] getPVPDetails(List<Pair<Integer, Integer>> players) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.PVP_DETAILS.getValue());
		mplew.write(1);
		mplew.write(0);
		mplew.writeInt(players.size());
		for (Pair<Integer, Integer> pl : players) {
			mplew.writeInt(pl.left);
			mplew.write(pl.right);
		}

		return mplew.getPacket();
	}

	public static byte[] enablePVP(boolean enabled) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.PVP_ENABLED.getValue());
		mplew.write(enabled ? 1 : 2);

		return mplew.getPacket();
	}

	public static byte[] getPVPScore(int score, boolean kill) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.PVP_SCORE.getValue());
		mplew.writeInt(score);
		mplew.write(kill ? 1 : 0);

		return mplew.getPacket();
	}

	public static byte[] getPVPResult(List<Pair<Integer, MapleCharacter>> flags, int exp, int winningTeam, int playerTeam) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.PVP_RESULT.getValue());
		mplew.writeInt(flags.size());
		for (Pair<Integer, MapleCharacter> f : flags) {
			mplew.writeInt(f.right.getId());
			mplew.writeMapleAsciiString(f.right.getName());
			mplew.writeInt(f.left);
			mplew.writeShort(f.right.getTeam() + 1);
			mplew.writeInt(0);
			mplew.writeInt(0);
		}
		mplew.writeZeroBytes(24);
		mplew.writeInt(exp);
		mplew.write(0);
		mplew.writeShort(100);
		mplew.writeInt(0);
		mplew.writeInt(0);
		mplew.write(winningTeam);
		mplew.write(playerTeam);

		return mplew.getPacket();
	}

	public static byte[] getPVPTeam(List<Pair<Integer, String>> players) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.PVP_TEAM.getValue());
		mplew.writeInt(players.size());
		for (Pair<Integer, String> pl : players) {
			mplew.writeInt(pl.left);
			mplew.writeMapleAsciiString(pl.right);
			mplew.writeShort(2660);
		}

		return mplew.getPacket();
	}

	public static byte[] getPVPScoreboard(List<Pair<Integer, MapleCharacter>> flags, int type) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.PVP_SCOREBOARD.getValue());
		mplew.writeShort(flags.size());
		for (Pair<Integer, MapleCharacter> f : flags) {
			mplew.writeInt(f.right.getId());
			mplew.writeMapleAsciiString(f.right.getName());
			mplew.writeInt(f.left.intValue());
			mplew.write(type == 0 ? 0 : f.right.getTeam() + 1);
		}

		return mplew.getPacket();
	}

	public static byte[] getPVPPoints(int p1, int p2) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.PVP_POINTS.getValue());
		mplew.writeInt(p1);
		mplew.writeInt(p2);

		return mplew.getPacket();
	}

	public static byte[] getPVPKilled(String lastWords) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.PVP_KILLED.getValue());
		mplew.writeMapleAsciiString(lastWords);

		return mplew.getPacket();
	}

	public static byte[] getPVPMode(int mode) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.PVP_MODE.getValue());
		mplew.write(mode);

		return mplew.getPacket();
	}

	public static byte[] getPVPIceHPBar(int hp, int maxHp) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.PVP_ICEKNIGHT.getValue());
		mplew.writeInt(hp);
		mplew.writeInt(maxHp);

		return mplew.getPacket();
	}

	public static byte[] getCaptureFlags(MapleMap map) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.CAPTURE_FLAGS.getValue());
		mplew.writeRect(map.getArea(0));
		Pair<Point, Integer> guardian = map.getGuardians().get(0);
		mplew.writeInt(guardian.left.x);
		mplew.writeInt(guardian.left.y);
		mplew.writeRect(map.getArea(1));
		guardian = map.getGuardians().get(1);
		mplew.writeInt(guardian.left.x);
		mplew.writeInt(guardian.left.y);

		return mplew.getPacket();
	}

	public static byte[] getCapturePosition(MapleMap map) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		Point p1 = map.getPointOfItem(2910000);
		Point p2 = map.getPointOfItem(2910001);
		mplew.writeShort(SendPacketOpcode.CAPTURE_POSITION.getValue());
		mplew.write(p1 == null ? 0 : 1);
		if (p1 != null) {
			mplew.writeInt(p1.x);
			mplew.writeInt(p1.y);
		}
		mplew.write(p2 == null ? 0 : 1);
		if (p2 != null) {
			mplew.writeInt(p2.x);
			mplew.writeInt(p2.y);
		}

		return mplew.getPacket();
	}

	public static byte[] resetCapture() {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.CAPTURE_RESET.getValue());

		return mplew.getPacket();
	}

	public static byte[] getMacros(SkillMacro[] macros) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.SKILL_MACRO.getValue());
		int count = 0;
		for (int i = 0; i < 5; i++) {
			if (macros[i] != null) {
				count++;
			}
		}
		mplew.write(count);
		for (int i = 0; i < 5; i++) {
			SkillMacro macro = macros[i];
			if (macro != null) {
				mplew.writeMapleAsciiString(macro.getName());
				mplew.write(macro.getShout());
				mplew.writeInt(macro.getSkill1());
				mplew.writeInt(macro.getSkill2());
				mplew.writeInt(macro.getSkill3());
			}
		}

		return mplew.getPacket();
	}

	public static byte[] getCharInfo(MapleCharacter chr) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.WARP_TO_MAP.getValue());
		mplew.writeShort(2);
		mplew.writeLong(1L);
		mplew.writeLong(2L);
		mplew.writeLong(chr.getClient().getChannel() - 1);
		mplew.write(0);
		mplew.write(1);
		mplew.writeInt(0);
		mplew.write(1);
		mplew.writeShort(0);
		chr.CRand().connectData(mplew);
		PacketHelper.addCharacterInfo(mplew, chr);

		mplew.writeZeroBytes(16);
		mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
		mplew.writeInt(100);
		mplew.write(0);
		mplew.write(1);

		return mplew.getPacket();
	}

	public static byte[] getWarpToMap(final MapleMap to, final int spawnPoint, final MapleCharacter chr) {

		final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.WARP_TO_MAP.getValue());

		mplew.writeShort(2);
		mplew.writeLong(1);
		mplew.writeLong(2);
		mplew.writeLong(chr.getClient().getChannel() - 1);
		mplew.write(0); // todo jump
		mplew.write(2);
		mplew.write(new byte[8]);// v93
		mplew.writeInt(to.getId());
		mplew.write(spawnPoint);
		mplew.writeInt(chr.getStat().getHp()); // bb - int
		mplew.write(0);// ?
		mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
		mplew.writeInt(100);
		mplew.writeShort(0);// v93

		return mplew.getPacket();
	}

	public static byte[] spawnFlags(List<Pair<String, Integer>> flags) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.LOGIN_WELCOME.getValue());
		mplew.write(flags == null ? 0 : flags.size());
		if (flags != null) {
			for (Pair<String, Integer> f : flags) {
				mplew.writeMapleAsciiString(f.left);
				mplew.write(f.right);
			}
		}

		return mplew.getPacket();
	}

	public static byte[] serverBlocked(int type) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.SERVER_BLOCKED.getValue());
		mplew.write(type);

		return mplew.getPacket();
	}

	public static byte[] pvpBlocked(int type) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.write(type);

		return mplew.getPacket();
	}

	public static byte[] showEquipEffect() {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.SHOW_EQUIP_EFFECT.getValue());

		return mplew.getPacket();
	}

	public static byte[] showEquipEffect(int team) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.SHOW_EQUIP_EFFECT.getValue());
		mplew.writeShort(team);

		return mplew.getPacket();
	}

	public static byte[] multiChat(String name, String chattext, int mode) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.MULTICHAT.getValue());
		mplew.write(mode);
		mplew.writeMapleAsciiString(name);
		mplew.writeMapleAsciiString(chattext);

		return mplew.getPacket();
	}

	public static byte[] getFindReplyWithCS(String target, boolean buddy) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.WHISPER.getValue());
		mplew.write(buddy ? 72 : 9);
		mplew.writeMapleAsciiString(target);
		mplew.write(2);
		mplew.writeInt(-1);

		return mplew.getPacket();
	}

	public static byte[] getFindReplyWithMTS(String target, boolean buddy) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.WHISPER.getValue());
		mplew.write(buddy ? 72 : 9);
		mplew.writeMapleAsciiString(target);
		mplew.write(0);
		mplew.writeInt(-1);

		return mplew.getPacket();
	}

	public static byte[] getWhisper(String sender, int channel, String text) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.WHISPER.getValue());
		mplew.write(18);
		mplew.writeMapleAsciiString(sender);
		mplew.writeShort(channel - 1);
		mplew.writeMapleAsciiString(text);

		return mplew.getPacket();
	}

	public static byte[] getWhisperReply(String target, byte reply) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.WHISPER.getValue());
		mplew.write(10);
		mplew.writeMapleAsciiString(target);
		mplew.write(reply);

		return mplew.getPacket();
	}

	public static byte[] getFindReplyWithMap(String target, int mapid, boolean buddy) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.WHISPER.getValue());
		mplew.write(buddy ? 72 : 9);
		mplew.writeMapleAsciiString(target);
		mplew.write(1);
		mplew.writeInt(mapid);
		mplew.writeZeroBytes(8);

		return mplew.getPacket();
	}

	public static byte[] getFindReply(String target, int channel, boolean buddy) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.WHISPER.getValue());
		mplew.write(buddy ? 72 : 9);
		mplew.writeMapleAsciiString(target);
		mplew.write(3);
		mplew.writeInt(channel - 1);

		return mplew.getPacket();
	}

	public static final byte[] MapEff(String path) {
		return environmentChange(path, 3);
	}

	public static final byte[] MapNameDisplay(int mapid) {
		return environmentChange("maplemap/enter/" + mapid, 3);
	}

	public static final byte[] Aran_Start() {
		return environmentChange("Aran/balloon", 4);
	}

	public static byte[] musicChange(String song) {
		return environmentChange(song, 6);
	}

	public static byte[] showEffect(String effect) {
		return environmentChange(effect, 3);
	}

	public static byte[] playSound(String sound) {
		return environmentChange(sound, 4);
	}

	public static byte[] environmentChange(String env, int mode) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.BOSS_ENV.getValue());
		mplew.write(mode);
		mplew.writeMapleAsciiString(env);

		return mplew.getPacket();
	}

	public static byte[] trembleEffect(int type, int delay) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.BOSS_ENV.getValue());
		mplew.write(1);
		mplew.write(type);
		mplew.writeInt(delay);

		return mplew.getPacket();
	}

	public static byte[] environmentMove(String env, int mode) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.MOVE_ENV.getValue());
		mplew.writeMapleAsciiString(env);
		mplew.writeInt(mode);

		return mplew.getPacket();
	}

	public static byte[] getUpdateEnvironment(MapleMap map) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.UPDATE_ENV.getValue());
		mplew.writeInt(map.getEnvironment().size());
		for (Map.Entry<String, Integer> mp : map.getEnvironment().entrySet()) {
			mplew.writeMapleAsciiString(mp.getKey());
			mplew.writeInt(mp.getValue());
		}

		return mplew.getPacket();
	}

	public static byte[] startMapEffect(String msg, int itemid, boolean active) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.MAP_EFFECT.getValue());
		mplew.write(active ? 0 : 1);

		mplew.writeInt(itemid);
		if (active) {
			mplew.writeMapleAsciiString(msg);
		}
		return mplew.getPacket();
	}

	public static byte[] removeMapEffect() {
		return startMapEffect(null, 0, false);
	}

	public static byte[] GameMaster_Func(int value) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.GM_EFFECT.getValue());
		mplew.write(value);
		mplew.writeZeroBytes(17);

		return mplew.getPacket();
	}

	public static byte[] showOXQuiz(int questionSet, int questionId, boolean askQuestion) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.OX_QUIZ.getValue());
		mplew.write(askQuestion ? 1 : 0);
		mplew.write(questionSet);
		mplew.writeShort(questionId);

		return mplew.getPacket();
	}

	public static byte[] showEventInstructions() {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.GMEVENT_INSTRUCTIONS.getValue());
		mplew.write(0);

		return mplew.getPacket();
	}

	public static byte[] getPVPClock(int type, int time) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.CLOCK.getValue());
		mplew.write(3);
		mplew.write(type);
		mplew.writeInt(time);

		return mplew.getPacket();
	}

	public static byte[] getClock(int time) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.CLOCK.getValue());
		mplew.write(2);
		mplew.writeInt(time);

		return mplew.getPacket();
	}

	public static byte[] getClockTime(int hour, int min, int sec) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.CLOCK.getValue());
		mplew.write(1);
		mplew.write(hour);
		mplew.write(min);
		mplew.write(sec);

		return mplew.getPacket();
	}

	public static byte[] boatPacket(int effect, int mode) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.BOAT_MOVE.getValue());
		mplew.write(effect);
		mplew.write(mode);

		return mplew.getPacket();
	}

	public static byte[] setBoatState(int effect) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.BOAT_STATE.getValue());
		mplew.write(effect);
		mplew.write(1);

		return mplew.getPacket();
	}

	public static byte[] stopClock() {
		return getPacketFromHexString(Integer.toHexString(SendPacketOpcode.STOP_CLOCK.getValue()) + " 00");
	}

	public static byte[] showAriantScoreBoard() {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.ARIANT_SCOREBOARD.getValue());

		return mplew.getPacket();
	}

	public static byte[] sendPyramidUpdate(int amount) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.PYRAMID_UPDATE.getValue());
		mplew.writeInt(amount);

		return mplew.getPacket();
	}

	public static byte[] sendPyramidResult(byte rank, int amount) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.PYRAMID_RESULT.getValue());
		mplew.write(rank);
		mplew.writeInt(amount);

		return mplew.getPacket();
	}

	public static byte[] quickSlot(String skil) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.QUICK_SLOT.getValue());
		mplew.write(skil == null ? 0 : 1);
		if (skil != null) {
			String[] slots = skil.split(",");
			for (int i = 0; i < 8; i++) {
				mplew.writeInt(Integer.parseInt(slots[i]));
			}
		}

		return mplew.getPacket();
	}

	public static byte[] getMovingPlatforms(MapleMap map) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.MOVE_PLATFORM.getValue());
		mplew.writeInt(map.getPlatforms().size());
		for (MapleNodes.MaplePlatform mp : map.getPlatforms()) {
			mplew.writeMapleAsciiString(mp.name);
			mplew.writeInt(mp.start);
			mplew.writeInt(mp.SN.size());
			for (int x = 0; x < mp.SN.size(); x++) {
				mplew.writeInt(mp.SN.get(x).intValue());
			}
			mplew.writeInt(mp.speed);
			mplew.writeInt(mp.x1);
			mplew.writeInt(mp.x2);
			mplew.writeInt(mp.y1);
			mplew.writeInt(mp.y2);
			mplew.writeInt(mp.x1);
			mplew.writeInt(mp.y1);
			mplew.writeShort(mp.r);
		}

		return mplew.getPacket();
	}

	public static byte[] sendPyramidKills(int amount) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.PYRAMID_KILL_COUNT.getValue());
		mplew.writeInt(amount);

		return mplew.getPacket();
	}

	public static byte[] sendPVPMaps() {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.PVP_INFO.getValue());
		mplew.write(3);
		for (int i = 0; i < 20; i++) {
			mplew.writeInt(10);
		}
		mplew.writeZeroBytes(124);
		mplew.writeShort(150);
		mplew.write(0);

		return mplew.getPacket();
	}

	public static byte[] gainForce(int oid, int count, int color) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.GAIN_FORCE.getValue());
		mplew.write(1);
		mplew.writeInt(oid);
		mplew.writeInt(0);
		mplew.write(1);
		mplew.writeInt(2);
		mplew.writeInt(color);
		mplew.writeInt(39);
		mplew.writeInt(5);
		mplew.writeInt(49);
		mplew.write(0);
		return mplew.getPacket();
	}

	public static byte[] getAndroidTalkStyle(int npc, String talk, int... args) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
		mplew.write(4);
		mplew.writeInt(npc);
		mplew.writeShort(10);
		mplew.writeMapleAsciiString(talk);
		mplew.write(args.length);

		for (int i = 0; i < args.length; i++) {
			mplew.writeInt(args[i]);
		}
		return mplew.getPacket();
	}

	public static byte[] achievementRatio(int amount) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.ACHIEVEMENT_RATIO.getValue());
		mplew.writeInt(amount);

		return mplew.getPacket();
	}

	public static byte[] getPublicNPCInfo() {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.PUBLIC_NPC.getValue());
		mplew.write(GameConstants.publicNpcIds.length);
		for (int i = 0; i < GameConstants.publicNpcIds.length; i++) {
			mplew.writeMapleAsciiString("");
			mplew.writeInt(GameConstants.publicNpcIds[i]);
			mplew.writeInt(i);
			mplew.writeInt(0);
			mplew.writeMapleAsciiString(GameConstants.publicNpcs[i]);
		}

		return mplew.getPacket();
	}

	public static byte[] spawnPlayerMapobject(MapleCharacter chr) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.SPAWN_PLAYER.getValue());
		mplew.writeInt(chr.getId());
		mplew.write(chr.getLevel());
		mplew.writeMapleAsciiString(chr.getName());
		MapleQuestStatus ultExplorer = chr.getQuestNoAdd(MapleQuest.getInstance(111111));
		if ((ultExplorer != null) && (ultExplorer.getCustomData() != null))
			mplew.writeMapleAsciiString(ultExplorer.getCustomData());
		else {
			mplew.writeMapleAsciiString("");
		}
		if (chr.getGuildId() <= 0) {
			mplew.writeInt(0);
			mplew.writeInt(0);
		} else {
			MapleGuild gs = World.Guild.getGuild(chr.getGuildId());
			if (gs != null) {
				mplew.writeMapleAsciiString(gs.getName());
				mplew.writeShort(gs.getLogoBG());
				mplew.write(gs.getLogoBGColor());
				mplew.writeShort(gs.getLogo());
				mplew.write(gs.getLogoColor());
			} else {
				mplew.writeInt(0);
				mplew.writeInt(0);
			}
		}

		final List<Pair<Integer, Integer>> buffvalue = new ArrayList<Pair<Integer, Integer>>();
		final int[] mask = new int[GameConstants.MAX_BUFFSTAT];
		mask[0] |= DEFAULT_BUFFMASK;
		mask[1] |= 0xA00000; // 0x200000 + 0x800000

		if ((chr.getBuffedValue(MapleBuffStat.DARKSIGHT) != null) || (chr.isHidden())) {
			mask[MapleBuffStat.DARKSIGHT.getPosition(true)] |= MapleBuffStat.DARKSIGHT.getValue();
		}
		if (chr.getBuffedValue(MapleBuffStat.SOULARROW) != null) {
			mask[MapleBuffStat.SOULARROW.getPosition(true)] |= MapleBuffStat.SOULARROW.getValue();
		}
		if (chr.getBuffedValue(MapleBuffStat.COMBO) != null) {
			mask[MapleBuffStat.COMBO.getPosition(true)] |= MapleBuffStat.COMBO.getValue();
			buffvalue.add(new Pair<Integer, Integer>(chr.getBuffedValue(MapleBuffStat.COMBO), 1));
		}
		if (chr.getBuffedValue(MapleBuffStat.WK_CHARGE) != null) {
			mask[MapleBuffStat.WK_CHARGE.getPosition(true)] |= MapleBuffStat.WK_CHARGE.getValue();
			buffvalue.add(new Pair<Integer, Integer>(chr.getBuffedValue(MapleBuffStat.WK_CHARGE), 2));
			buffvalue.add(new Pair<Integer, Integer>(chr.getBuffSource(MapleBuffStat.WK_CHARGE), 3));
		}
		if (chr.getBuffedValue(MapleBuffStat.SHADOWPARTNER) != null) {
			mask[MapleBuffStat.SHADOWPARTNER.getPosition(true)] |= MapleBuffStat.SHADOWPARTNER.getValue();
			buffvalue.add(new Pair<Integer, Integer>(chr.getBuffedValue(MapleBuffStat.SHADOWPARTNER), 2));
			buffvalue.add(new Pair<Integer, Integer>(chr.getBuffSource(MapleBuffStat.SHADOWPARTNER), 3));
		}

		if (chr.getBuffedValue(MapleBuffStat.MORPH) != null) {
			mask[MapleBuffStat.MORPH.getPosition(true)] |= MapleBuffStat.MORPH.getValue();
			buffvalue.add(new Pair<Integer, Integer>(chr.getStatForBuff(MapleBuffStat.MORPH).getMorph(chr), 2));
			buffvalue.add(new Pair<Integer, Integer>(chr.getBuffSource(MapleBuffStat.MORPH), 3));
		}
		if (chr.getBuffedValue(MapleBuffStat.BERSERK_FURY) != null) {
			mask[MapleBuffStat.BERSERK_FURY.getPosition(true)] |= MapleBuffStat.BERSERK_FURY.getValue();
		}
		if (chr.getBuffedValue(MapleBuffStat.DIVINE_BODY) != null) {
			mask[MapleBuffStat.DIVINE_BODY.getPosition(true)] |= MapleBuffStat.DIVINE_BODY.getValue();
		}

		if (chr.getBuffedValue(MapleBuffStat.WIND_WALK) != null) {
			mask[MapleBuffStat.WIND_WALK.getPosition(true)] |= MapleBuffStat.WIND_WALK.getValue();
			buffvalue.add(new Pair<Integer, Integer>(chr.getBuffedValue(MapleBuffStat.WIND_WALK), 2));
			buffvalue.add(new Pair<Integer, Integer>(chr.getTrueBuffSource(MapleBuffStat.WIND_WALK), 3));
		}
		if (chr.getBuffedValue(MapleBuffStat.PYRAMID_PQ) != null) {
			mask[MapleBuffStat.PYRAMID_PQ.getPosition(true)] |= MapleBuffStat.PYRAMID_PQ.getValue();
			buffvalue.add(new Pair<Integer, Integer>(chr.getBuffedValue(MapleBuffStat.PYRAMID_PQ), 2));
			buffvalue.add(new Pair<Integer, Integer>(chr.getTrueBuffSource(MapleBuffStat.PYRAMID_PQ), 3));
		}
		if (chr.getBuffedValue(MapleBuffStat.SOARING) != null) {
			mask[MapleBuffStat.SOARING.getPosition(true)] |= MapleBuffStat.SOARING.getValue();
			buffvalue.add(new Pair<Integer, Integer>(chr.getBuffedValue(MapleBuffStat.SOARING), 2));
			buffvalue.add(new Pair<Integer, Integer>(chr.getTrueBuffSource(MapleBuffStat.SOARING), 3));
		}
		if (chr.getBuffedValue(MapleBuffStat.OWL_SPIRIT) != null) {
			mask[MapleBuffStat.OWL_SPIRIT.getPosition(true)] |= MapleBuffStat.OWL_SPIRIT.getValue();
			buffvalue.add(new Pair<Integer, Integer>(chr.getBuffedValue(MapleBuffStat.OWL_SPIRIT), 2));
			buffvalue.add(new Pair<Integer, Integer>(chr.getTrueBuffSource(MapleBuffStat.OWL_SPIRIT), 3));
		}
		if (chr.getBuffedValue(MapleBuffStat.FINAL_CUT) != null) {
			mask[MapleBuffStat.FINAL_CUT.getPosition(true)] |= MapleBuffStat.FINAL_CUT.getValue();
			buffvalue.add(new Pair<Integer, Integer>(chr.getBuffedValue(MapleBuffStat.FINAL_CUT), 2));
			buffvalue.add(new Pair<Integer, Integer>(chr.getTrueBuffSource(MapleBuffStat.FINAL_CUT), 3));
		}

		if (chr.getBuffedValue(MapleBuffStat.TORNADO) != null) {
			mask[MapleBuffStat.TORNADO.getPosition(true)] |= MapleBuffStat.TORNADO.getValue();
			buffvalue.add(new Pair<Integer, Integer>(chr.getBuffedValue(MapleBuffStat.TORNADO), 2));
			buffvalue.add(new Pair<Integer, Integer>(chr.getTrueBuffSource(MapleBuffStat.TORNADO), 3));
		}
		if (chr.getBuffedValue(MapleBuffStat.INFILTRATE) != null) {
			mask[MapleBuffStat.INFILTRATE.getPosition(true)] |= MapleBuffStat.INFILTRATE.getValue();
		}
		if (chr.getBuffedValue(MapleBuffStat.MECH_CHANGE) != null) {
			mask[MapleBuffStat.MECH_CHANGE.getPosition(true)] |= MapleBuffStat.MECH_CHANGE.getValue();
			buffvalue.add(new Pair<Integer, Integer>(chr.getBuffedValue(MapleBuffStat.MECH_CHANGE), 2));
			buffvalue.add(new Pair<Integer, Integer>(chr.getTrueBuffSource(MapleBuffStat.MECH_CHANGE), 3));
		}
		if (chr.getBuffedValue(MapleBuffStat.DARK_AURA) != null) {
			mask[MapleBuffStat.DARK_AURA.getPosition(true)] |= MapleBuffStat.DARK_AURA.getValue();
			buffvalue.add(new Pair<Integer, Integer>(chr.getBuffedValue(MapleBuffStat.DARK_AURA), 2));
			buffvalue.add(new Pair<Integer, Integer>(chr.getTrueBuffSource(MapleBuffStat.DARK_AURA), 3));
		}
		if (chr.getBuffedValue(MapleBuffStat.BLUE_AURA) != null) {
			mask[MapleBuffStat.BLUE_AURA.getPosition(true)] |= MapleBuffStat.BLUE_AURA.getValue();
			buffvalue.add(new Pair<Integer, Integer>(chr.getBuffedValue(MapleBuffStat.BLUE_AURA), 2));
			buffvalue.add(new Pair<Integer, Integer>(chr.getTrueBuffSource(MapleBuffStat.BLUE_AURA), 3));
		}
		if (chr.getBuffedValue(MapleBuffStat.YELLOW_AURA) != null) {
			mask[MapleBuffStat.YELLOW_AURA.getPosition(true)] |= MapleBuffStat.YELLOW_AURA.getValue();
			buffvalue.add(new Pair<Integer, Integer>(chr.getBuffedValue(MapleBuffStat.YELLOW_AURA), 2));
			buffvalue.add(new Pair<Integer, Integer>(chr.getTrueBuffSource(MapleBuffStat.YELLOW_AURA), 3));
		}
		if (chr.getBuffedValue(MapleBuffStat.DIVINE_SHIELD) != null) {
			mask[MapleBuffStat.DIVINE_SHIELD.getPosition(true)] |= MapleBuffStat.DIVINE_SHIELD.getValue();
		}
		if (chr.getBuffedValue(MapleBuffStat.GIANT_POTION) != null) {
			mask[MapleBuffStat.GIANT_POTION.getPosition(true)] |= MapleBuffStat.GIANT_POTION.getValue();
			buffvalue.add(new Pair<Integer, Integer>(chr.getBuffedValue(MapleBuffStat.GIANT_POTION), 2));
			buffvalue.add(new Pair<Integer, Integer>(chr.getTrueBuffSource(MapleBuffStat.GIANT_POTION), 3));
		}

		if (chr.getBuffedValue(MapleBuffStat.WATER_SHIELD) != null) {
			mask[MapleBuffStat.WATER_SHIELD.getPosition(true)] |= MapleBuffStat.WATER_SHIELD.getValue();
		}

		if (chr.getBuffedValue(MapleBuffStat.DARK_METAMORPHOSIS) != null) {
			mask[MapleBuffStat.DARK_METAMORPHOSIS.getPosition(true)] |= MapleBuffStat.DARK_METAMORPHOSIS.getValue();
			buffvalue.add(new Pair<Integer, Integer>(chr.getBuffedValue(MapleBuffStat.DARK_METAMORPHOSIS), 2));
			buffvalue.add(new Pair<Integer, Integer>(chr.getBuffSource(MapleBuffStat.DARK_METAMORPHOSIS), 3));
		}
		if (chr.getBuffedValue(MapleBuffStat.SPIRIT_SURGE) != null) {
			mask[MapleBuffStat.SPIRIT_SURGE.getPosition(true)] |= MapleBuffStat.SPIRIT_SURGE.getValue();
			buffvalue.add(new Pair<Integer, Integer>(chr.getBuffedValue(MapleBuffStat.SPIRIT_SURGE), 2));
			buffvalue.add(new Pair<Integer, Integer>(chr.getBuffSource(MapleBuffStat.SPIRIT_SURGE), 3));
		}

		if (chr.getBuffedValue(MapleBuffStat.FAMILIAR_SHADOW) != null) {
			mask[MapleBuffStat.FAMILIAR_SHADOW.getPosition(true)] |= MapleBuffStat.FAMILIAR_SHADOW.getValue();
			buffvalue.add(new Pair<Integer, Integer>(chr.getBuffedValue(MapleBuffStat.FAMILIAR_SHADOW), 3));
			buffvalue.add(new Pair<Integer, Integer>(chr.getStatForBuff(MapleBuffStat.FAMILIAR_SHADOW).getCharColor(), 3));
		}

		for (int i = 0; i < mask.length; i++) {
			mplew.writeInt(mask[i]);
		}

		for (Pair<Integer, Integer> i : buffvalue) {
			if (i.right == 3)
				mplew.writeInt(i.left);
			else if (i.right == 2)
				mplew.writeShort(i.left);
			else if (i.right == 1) {
				mplew.write(i.left);
			}
		}

		mplew.writeInt(-1);
		int CHAR_MAGIC_SPAWN = Randomizer.nextInt();

		mplew.writeInt(0);
		mplew.writeLong(0L);
		mplew.write(1);
		mplew.writeInt(CHAR_MAGIC_SPAWN);
		mplew.writeShort(0);
		mplew.writeLong(0L);
		mplew.write(1);
		mplew.writeInt(CHAR_MAGIC_SPAWN);
		mplew.writeShort(0);
		mplew.writeLong(0L);
		mplew.write(1);
		mplew.writeInt(CHAR_MAGIC_SPAWN);
		mplew.writeShort(0);
		int buffSrc = chr.getBuffSource(MapleBuffStat.MONSTER_RIDING);
		if (buffSrc > 0) {
			Item c_mount = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -118);
			Item mount = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -18);
			if ((GameConstants.getMountItem(buffSrc, chr) == 0) && (c_mount != null) && (chr.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -119) != null))
				mplew.writeInt(c_mount.getItemId());
			else if ((GameConstants.getMountItem(buffSrc, chr) == 0) && (mount != null) && (chr.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -19) != null))
				mplew.writeInt(mount.getItemId());
			else {
				mplew.writeInt(GameConstants.getMountItem(buffSrc, chr));
			}
			mplew.writeInt(buffSrc);
		} else {
			mplew.writeLong(0L);
		}
		mplew.write(1);
		mplew.writeInt(CHAR_MAGIC_SPAWN);
		mplew.writeLong(0L);
		mplew.write(1);
		mplew.writeInt(CHAR_MAGIC_SPAWN);
		mplew.writeInt(1);
		mplew.writeLong(0L);
		mplew.write(0);
		mplew.writeShort(0);
		mplew.write(1);
		mplew.writeInt(CHAR_MAGIC_SPAWN);
		mplew.writeZeroBytes(16);
		mplew.write(1);
		mplew.writeInt(CHAR_MAGIC_SPAWN);

		mplew.writeShort(0);
		mplew.writeShort(chr.getJob());
		mplew.writeShort(0);
		PacketHelper.addCharLook(mplew, chr, true);
		mplew.writeInt(0);
		mplew.writeInt(0);

		mplew.writeInt(Math.min(250, chr.getInventory(MapleInventoryType.CASH).countById(5110000)));
		mplew.writeInt(0);
		mplew.writeInt(0);

		mplew.writeInt(0);
		mplew.writeInt(0);
		mplew.writeInt(0);

		MapleQuestStatus stat = chr.getQuestNoAdd(MapleQuest.getInstance(124000));
		mplew.writeInt((stat != null) && (stat.getCustomData() != null) ? Integer.valueOf(stat.getCustomData()) : 0);
		mplew.writeInt(0);
		mplew.writeInt(chr.getItemEffect());
		mplew.writeInt(GameConstants.getInventoryType(chr.getChair()) == MapleInventoryType.SETUP ? chr.getChair() : 0);
		mplew.writeInt(0);

		mplew.writePos(chr.getTruePosition());
		mplew.write(chr.getStance());
		mplew.writeShort(0);
		mplew.write(0);
		mplew.write(0);

		mplew.write(1);
		mplew.write(0);

		mplew.writeInt(chr.getMount().getLevel());
		mplew.writeInt(chr.getMount().getExp());
		mplew.writeInt(chr.getMount().getFatigue());

		PacketHelper.addAnnounceBox(mplew, chr);
		mplew.write((chr.getChalkboard() != null) && (chr.getChalkboard().length() > 0) ? 1 : 0);
		if ((chr.getChalkboard() != null) && (chr.getChalkboard().length() > 0)) {
			mplew.writeMapleAsciiString(chr.getChalkboard());
		}

		Triple<List<MapleRing>, List<MapleRing>, List<MapleRing>> rings = chr.getRings(false);
		addRingInfo(mplew, rings.getLeft());
		addRingInfo(mplew, rings.getMid());
		addMRingInfo(mplew, rings.getRight(), chr);

		mplew.write(chr.getStat().Berserk ? 1 : 0);
		mplew.writeInt(0);
		mplew.write(0);

		mplew.writeInt(0);
		boolean pvp = chr.inPVP();
		if (pvp) {
			mplew.write(Integer.parseInt(chr.getEventInstance().getProperty("type")));
		}
		if (chr.getCarnivalParty() != null)
			mplew.write(chr.getCarnivalParty().getTeam());
		else if (GameConstants.isTeamMap(chr.getMapId())) {
			mplew.write(chr.getTeam() + (pvp ? 1 : 0));
		}

		return mplew.getPacket();
	}

	public static byte[] removePlayerFromMap(int cid) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.REMOVE_PLAYER_FROM_MAP.getValue());
		mplew.writeInt(cid);

		return mplew.getPacket();
	}

	public static byte[] getChatText(int cidfrom, String text, boolean whiteBG, int show) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.CHATTEXT.getValue());
		mplew.writeInt(cidfrom);
		mplew.write(whiteBG ? 1 : 0);
		mplew.writeMapleAsciiString(text);
		mplew.write(show);

		return mplew.getPacket();
	}

	public static byte[] getScrollEffect(int chr, Equip.ScrollResult scrollSuccess, boolean legendarySpirit, boolean whiteScroll) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.SHOW_SCROLL_EFFECT.getValue());
		mplew.writeInt(chr);
		mplew.write(scrollSuccess == Equip.ScrollResult.SUCCESS ? 1 : scrollSuccess == Equip.ScrollResult.CURSE ? 2 : 0);
		mplew.write(legendarySpirit ? 1 : 0);
		mplew.writeInt(0);
		mplew.writeInt(0);
		mplew.writeInt(0);
		mplew.write(0);
		mplew.write(0);

		return mplew.getPacket();
	}

	public static byte[] showMagnifyingEffect(int chr, short pos) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.SHOW_MAGNIFYING_EFFECT.getValue());
		mplew.writeInt(chr);
		mplew.writeShort(pos);

		return mplew.getPacket();
	}

	public static byte[] showPotentialReset(boolean fireworks, int chr, boolean success, int itemid) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(fireworks ? SendPacketOpcode.SHOW_FIREWORKS_EFFECT.getValue() : SendPacketOpcode.SHOW_POTENTIAL_RESET.getValue());
		mplew.writeInt(chr);
		mplew.write(success ? 1 : 0);
		mplew.writeInt(itemid);

		return mplew.getPacket();
	}

	public static byte[] showNebuliteEffect(int chr, boolean success) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.SHOW_NEBULITE_EFFECT.getValue());
		mplew.writeInt(chr);
		mplew.write(success ? 1 : 0);
		mplew.writeMapleAsciiString(success ? "Successfully mounted Nebulite." : "Failed to mount Nebulite.");

		return mplew.getPacket();
	}

	public static byte[] useNebuliteFusion(int cid, int itemId, boolean success) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.SHOW_FUSION_EFFECT.getValue());
		mplew.writeInt(cid);
		mplew.write(success ? 1 : 0);
		mplew.writeInt(itemId);

		return mplew.getPacket();
	}

	public static byte[] pvpAttack(int cid, int playerLevel, int skill, int skillLevel, int speed, int mastery, int projectile, int attackCount, int chargeTime, int stance, int direction, int range, int linkSkill, int linkSkillLevel, boolean movementSkill, boolean pushTarget, boolean pullTarget, List<AttackPair> attack) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.PVP_ATTACK.getValue());
		mplew.writeInt(cid);
		mplew.write(playerLevel);
		mplew.writeInt(skill);
		mplew.write(skillLevel);
		mplew.writeInt(linkSkill != skill ? linkSkill : 0);
		mplew.write(linkSkillLevel != skillLevel ? linkSkillLevel : 0);
		mplew.write(direction);
		mplew.write(movementSkill ? 1 : 0);
		mplew.write(pushTarget ? 1 : 0);
		mplew.write(pullTarget ? 1 : 0);
		mplew.write(0);
		mplew.writeShort(stance);
		mplew.write(speed);
		mplew.write(mastery);
		mplew.writeInt(projectile);
		mplew.writeInt(chargeTime);
		mplew.writeInt(range);
		mplew.write(attack.size());
		mplew.write(0);
		mplew.writeInt(0);
		mplew.write(attackCount);
		mplew.write(0);
		for (AttackPair p : attack) {
			mplew.writeInt(p.objectid);
			mplew.writeInt(0);
			mplew.writePos(p.point);
			mplew.write(0);
			mplew.writeInt(0);
			for (Pair<?, ?> atk : p.attack) {
				mplew.writeInt(((Integer) atk.left).intValue());
				mplew.writeInt(0);
				mplew.write(((Boolean) atk.right).booleanValue() ? 1 : 0);
				mplew.writeShort(0);
			}
		}

		return mplew.getPacket();
	}

	public static byte[] getPVPMist(int cid, int mistSkill, int mistLevel, int damage) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.PVP_MIST.getValue());
		mplew.writeInt(cid);
		mplew.writeInt(mistSkill);
		mplew.write(mistLevel);
		mplew.writeInt(damage);
		mplew.write(8);
		mplew.writeInt(1000);

		return mplew.getPacket();
	}

	public static byte[] pvpCool(int cid, List<Integer> attack) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.PVP_COOL.getValue());
		mplew.writeInt(cid);
		mplew.write(attack.size());
		for (Iterator<Integer> i$ = attack.iterator(); i$.hasNext();) {
			int b = i$.next().intValue();
			mplew.writeInt(b);
		}

		return mplew.getPacket();
	}

	public static byte[] teslaTriangle(int cid, int sum1, int sum2, int sum3) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.TESLA_TRIANGLE.getValue());
		mplew.writeInt(cid);
		mplew.writeInt(sum1);
		mplew.writeInt(sum2);
		mplew.writeInt(sum3);

		return mplew.getPacket();
	}

	public static byte[] followEffect(int initiator, int replier, Point toMap) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.FOLLOW_EFFECT.getValue());
		mplew.writeInt(initiator);
		mplew.writeInt(replier);
		if (replier == 0) {
			mplew.write(toMap == null ? 0 : 1);
			if (toMap != null) {
				mplew.writeInt(toMap.x);
				mplew.writeInt(toMap.y);
			}
		}

		return mplew.getPacket();
	}

	public static byte[] showPQReward(int cid) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.SHOW_PQ_REWARD.getValue());
		mplew.writeInt(cid);
		for (int i = 0; i < 6; i++) {
			mplew.write(0);
		}

		return mplew.getPacket();
	}

	public static byte[] craftMake(int cid, int something, int time) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.CRAFT_EFFECT.getValue());
		mplew.writeInt(cid);
		mplew.writeInt(something);
		mplew.writeInt(time);

		return mplew.getPacket();
	}

	public static byte[] craftFinished(int cid, int craftID, int ranking, int itemId, int quantity, int exp) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.CRAFT_COMPLETE.getValue());
		mplew.writeInt(cid);
		mplew.writeInt(craftID);
		mplew.writeInt(ranking);
		mplew.writeInt(itemId);
		mplew.writeInt(quantity);
		mplew.writeInt(exp);

		return mplew.getPacket();
	}

	public static byte[] harvestResult(int cid, boolean success) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.HARVESTED.getValue());
		mplew.writeInt(cid);
		mplew.write(success ? 1 : 0);

		return mplew.getPacket();
	}

	public static byte[] playerDamaged(int cid, int dmg) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.PLAYER_DAMAGED.getValue());
		mplew.writeInt(cid);
		mplew.writeInt(dmg);

		return mplew.getPacket();
	}

	public static byte[] showPyramidEffect(int chr) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.NETT_PYRAMID.getValue());
		mplew.writeInt(chr);
		mplew.write(1);
		mplew.writeInt(0);
		mplew.writeInt(0);

		return mplew.getPacket();
	}

	public static byte[] pamsSongEffect(int cid) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.PAMS_SONG.getValue());
		mplew.writeInt(cid);

		return mplew.getPacket();
	}

	public static byte[] spawnDragon(MapleDragon d) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.DRAGON_SPAWN.getValue());
		mplew.writeInt(d.getOwner());
		mplew.writeInt(d.getPosition().x);
		mplew.writeInt(d.getPosition().y);
		mplew.write(d.getStance());
		mplew.writeShort(0);
		mplew.writeShort(d.getJobId());

		return mplew.getPacket();
	}

	public static byte[] removeDragon(int chrid) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.DRAGON_REMOVE.getValue());
		mplew.writeInt(chrid);

		return mplew.getPacket();
	}

	public static byte[] moveDragon(MapleDragon d, Point startPos, List<LifeMovementFragment> moves) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.DRAGON_MOVE.getValue());
		mplew.writeInt(d.getOwner());
		mplew.writePos(startPos);
		mplew.writeInt(0);
		PacketHelper.serializeMovementList(mplew, moves);

		return mplew.getPacket();
	}

	public static byte[] spawnAndroid(MapleCharacter cid, MapleAndroid android) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.ANDROID_SPAWN.getValue());
		mplew.writeInt(cid.getId());
		mplew.write(android.getItemId() == 1662006 ? 5 : android.getItemId() - 1661999);
		mplew.writePos(android.getPos());
		mplew.write(android.getStance());
		mplew.writeShort(0);
		mplew.writeShort(0);
		mplew.writeShort(android.getHair() - 30000);
		mplew.writeShort(android.getFace() - 20000);
		mplew.writeMapleAsciiString(android.getName());
		for (short i = -1200; i > -1207; i = (short) (i - 1)) {
			Item item = cid.getInventory(MapleInventoryType.EQUIPPED).getItem(i);
			mplew.writeInt(item != null ? item.getItemId() : 0);
		}

		return mplew.getPacket();
	}

	public static byte[] moveAndroid(int cid, Point pos, List<LifeMovementFragment> res) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.ANDROID_MOVE.getValue());
		mplew.writeInt(cid);
		mplew.writePos(pos);
		mplew.writeInt(Integer.MAX_VALUE); // time left in milliseconds? this appears to go down...slowly 1377440900
		PacketHelper.serializeMovementList(mplew, res);
		return mplew.getPacket();
	}

	public static byte[] showAndroidEmotion(int cid, int animation) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.ANDROID_EMOTION.getValue());
		mplew.writeInt(cid);
		mplew.write(0);
		mplew.write(animation);

		return mplew.getPacket();
	}

	public static byte[] updateAndroidLook(boolean itemOnly, MapleCharacter cid, MapleAndroid android) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.ANDROID_UPDATE.getValue());
		mplew.writeInt(cid.getId());
		mplew.write(itemOnly ? 1 : 0);
		if (itemOnly) {
			for (short i = -1200; i > -1207; i = (short) (i - 1)) {
				Item item = cid.getInventory(MapleInventoryType.EQUIPPED).getItem(i);
				mplew.writeInt(item != null ? item.getItemId() : 0);
			}
		} else {
			mplew.writeShort(0);
			mplew.writeShort(android.getHair() - 30000);
			mplew.writeShort(android.getFace() - 20000);
			mplew.writeMapleAsciiString(android.getName());
		}

		return mplew.getPacket();
	}

	public static byte[] deactivateAndroid(int cid) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.ANDROID_DEACTIVATED.getValue());
		mplew.writeInt(cid);

		return mplew.getPacket();
	}

	public static byte[] removeFamiliar(int cid) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.SPAWN_FAMILIAR.getValue());
		mplew.writeInt(cid);
		mplew.writeShort(0);
		mplew.write(0);

		return mplew.getPacket();
	}

	public static byte[] spawnFamiliar(MonsterFamiliar mf, boolean spawn, boolean respawn) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(respawn ? SendPacketOpcode.RESPAWN_FAMILIAR.getValue() : SendPacketOpcode.SPAWN_FAMILIAR.getValue());
		mplew.writeInt(mf.getCharacterId());
		mplew.write(spawn ? 1 : 0);
		mplew.write(respawn ? 1 : 0);
		mplew.write(0);
		if (spawn) {
			mplew.writeInt(mf.getFamiliar());
			mplew.writeInt(mf.getFatigue());
			mplew.writeInt(mf.getVitality() * 300); // max fatigue
			mplew.writeMapleAsciiString(mf.getName());
			mplew.writePos(mf.getTruePosition());
			mplew.write(mf.getStance());
			mplew.writeShort(mf.getFh());
		}

		return mplew.getPacket();
	}

	public static byte[] addStolenSkill(int jobNum, int index, int skill, int level) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.UPDATE_STOLEN_SKILLS.getValue());
		mplew.write(1);
		mplew.write(0);
		mplew.writeInt(jobNum);
		mplew.writeInt(index);
		mplew.writeInt(skill);
		mplew.writeInt(level);
		mplew.writeInt(0);
		mplew.write(0);

		return mplew.getPacket();
	}

	public static byte[] removeStolenSkill(int jobNum, int index) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.UPDATE_STOLEN_SKILLS.getValue());
		mplew.write(1);
		mplew.write(3);
		mplew.writeInt(jobNum);
		mplew.writeInt(index);
		mplew.write(0);

		return mplew.getPacket();
	}

	public static byte[] replaceStolenSkill(int base, int skill) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.REPLACE_SKILLS.getValue());
		mplew.write(1);
		mplew.write(skill > 0 ? 1 : 0);
		mplew.writeInt(base);
		mplew.writeInt(skill);

		return mplew.getPacket();
	}

	public static byte[] gainCardStack(int oid, int runningId, int color, int skillid, int damage, int times) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.GAIN_FORCE.getValue());
		mplew.write(0);
		mplew.writeInt(oid);
		mplew.writeInt(1);
		mplew.writeInt(damage);
		mplew.writeInt(skillid);
		for (int i = 0; i < times; ++i) {
			mplew.write(1);
			mplew.writeInt((damage == 0) ? runningId + i : runningId);
			mplew.writeInt(color);
			mplew.writeInt(Randomizer.rand(15, 29));
			mplew.writeInt(Randomizer.rand(7, 11));
			mplew.writeInt(Randomizer.rand(0, 9));
		}
		mplew.write(0);

		return mplew.getPacket();
	}

	public static byte[] getCarteAnimation(int cid, int oid, int job, int total, int numDisplay) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.GAIN_FORCE.getValue());
		mplew.write(0);
		mplew.writeInt(cid);
		mplew.writeInt(1);

		mplew.writeInt(oid);
		mplew.writeInt(job == 2412 ? 24120002 : 24100003);
		mplew.write(1);
		for (int i = 1; i <= numDisplay; i++) {
			mplew.writeInt(total - (numDisplay - i));
			mplew.writeInt(job == 2412 ? 2 : 0);

			mplew.writeInt(15 + Randomizer.nextInt(15));
			mplew.writeInt(7 + Randomizer.nextInt(5));
			mplew.writeInt(Randomizer.nextInt(4));

			mplew.write(i == numDisplay ? 0 : 1);
		}

		return mplew.getPacket();
	}

	public static byte[] moveFamiliar(int cid, Point startPos, List<LifeMovementFragment> moves) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.MOVE_FAMILIAR.getValue());
		mplew.writeInt(cid);
		mplew.write(0);
		mplew.writePos(startPos);
		mplew.writeInt(0);
		PacketHelper.serializeMovementList(mplew, moves);

		return mplew.getPacket();
	}

	public static byte[] touchFamiliar(int cid, byte unk, int objectid, int type, int delay, int damage) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.TOUCH_FAMILIAR.getValue());
		mplew.writeInt(cid);
		mplew.write(0);
		mplew.write(unk);
		mplew.writeInt(objectid);
		mplew.writeInt(type);
		mplew.writeInt(delay);
		mplew.writeInt(damage);

		return mplew.getPacket();
	}

	public static byte[] familiarAttack(int cid, byte unk, List<Triple<Integer, Integer, List<Integer>>> attackPair) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.ATTACK_FAMILIAR.getValue());
		mplew.writeInt(cid);
		mplew.write(0);// familiar id?
		mplew.write(unk);
		mplew.write(attackPair.size());
		for (Triple<Integer, Integer, List<Integer>> s : attackPair) {
			mplew.writeInt(s.left);
			mplew.write(s.mid);
			mplew.write(s.right.size());
			for (int damage : s.right) {
				mplew.writeInt(damage);
			}
		}

		return mplew.getPacket();
	}

	public static byte[] renameFamiliar(MonsterFamiliar mf) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.RENAME_FAMILIAR.getValue());
		mplew.writeInt(mf.getCharacterId());
		mplew.write(0);
		mplew.writeInt(mf.getFamiliar());
		mplew.writeMapleAsciiString(mf.getName());

		return mplew.getPacket();
	}

	public static byte[] updateFamiliar(MonsterFamiliar mf) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.UPDATE_FAMILIAR.getValue());
		mplew.writeInt(mf.getCharacterId());
		mplew.writeInt(mf.getFamiliar());
		mplew.writeInt(mf.getFatigue());
		mplew.writeLong(PacketHelper.getTime(mf.getVitality() >= 3 ? System.currentTimeMillis() : -2L));

		return mplew.getPacket();
	}

	public static byte[] movePlayer(int cid, List<LifeMovementFragment> moves, Point startPos) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.MOVE_PLAYER.getValue());
		mplew.writeInt(cid);
		mplew.writePos(startPos);
		mplew.writeInt(0);
		PacketHelper.serializeMovementList(mplew, moves);

		return mplew.getPacket();
	}

	public static byte[] closeRangeAttack(int cid, int tbyte, int skill, int level, int display, byte speed, List<AttackPair> damage, boolean energy, int lvl, byte mastery, byte unk, int charge) {
		return addAttackInfo(energy ? 4 : 0, cid, tbyte, skill, level, display, speed, damage, lvl, mastery, unk, 0, null, 0);
	}

	public static byte[] rangedAttack(int cid, byte tbyte, int skill, int level, int display, byte speed, int itemid, List<AttackPair> damage, Point pos, int lvl, byte mastery, byte unk) {
		return addAttackInfo(1, cid, tbyte, skill, level, display, speed, damage, lvl, mastery, unk, itemid, pos, 0);
	}

	public static byte[] strafeAttack(int cid, byte tbyte, int skill, int level, int display, byte speed, int itemid, List<AttackPair> damage, Point pos, int lvl, byte mastery, byte unk, int ultLevel) {
		return addAttackInfo(2, cid, tbyte, skill, level, display, speed, damage, lvl, mastery, unk, itemid, pos, ultLevel);
	}

	public static byte[] magicAttack(int cid, int tbyte, int skill, int level, int display, byte speed, List<AttackPair> damage, int charge, int lvl, byte unk) {
		return addAttackInfo(3, cid, tbyte, skill, level, display, speed, damage, lvl, (byte) 0, unk, charge, null, 0);
	}

	public static byte[] addAttackInfo(int type, int cid, int tbyte, int skill, int level, int display, byte speed, List<AttackPair> damage, int lvl, byte mastery, byte unk, int charge, Point pos, int ultLevel) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		if (type == 0)
			mplew.writeShort(SendPacketOpcode.CLOSE_RANGE_ATTACK.getValue());
		else if ((type == 1) || (type == 2))
			mplew.writeShort(SendPacketOpcode.RANGED_ATTACK.getValue());
		else if (type == 3)
			mplew.writeShort(SendPacketOpcode.MAGIC_ATTACK.getValue());
		else {
			mplew.writeShort(SendPacketOpcode.ENERGY_ATTACK.getValue());
		}

		mplew.writeInt(cid);
		mplew.write(tbyte);
		mplew.write(lvl);
		if ((skill > 0) || (type == 3)) {
			mplew.write(level);
			mplew.writeInt(skill);
		} else {
			mplew.write(0);
		}

		if (type == 2) {
			mplew.write(ultLevel);
			if (ultLevel > 0) {
				mplew.writeInt(3220010);
			}
		}
		mplew.write(unk);
		mplew.writeShort(display);
		mplew.write(speed);
		mplew.write(mastery);
		mplew.writeInt(charge);
		for (AttackPair oned : damage) {
			if (oned.attack != null) {
				mplew.writeInt(oned.objectid);
				mplew.write(7);
				if (skill == 4211006) {
					mplew.write(oned.attack.size());
					for (Pair<?, ?> eachd : oned.attack)
						mplew.writeInt(((Integer) eachd.left).intValue());
				} else {
					for (Pair<?, ?> eachd : oned.attack) {
						mplew.write(((Boolean) eachd.right).booleanValue() ? 1 : 0);
						mplew.writeInt(((Integer) eachd.left).intValue());
					}
				}
			}
		}
		if ((type == 1) || (type == 2))
			mplew.writePos(pos);
		else if ((type == 3) && (charge > 0)) {
			mplew.writeInt(charge);
		}

		return mplew.getPacket();
	}

	public static byte[] skillEffect(MapleCharacter from, int skillId, byte level, short display, byte unk) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.SKILL_EFFECT.getValue());
		mplew.writeInt(from.getId());
		mplew.writeInt(skillId);
		mplew.write(level);
		mplew.writeShort(display);
		mplew.write(unk);

		return mplew.getPacket();
	}

	public static byte[] skillCancel(MapleCharacter from, int skillId) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.CANCEL_SKILL_EFFECT.getValue());
		mplew.writeInt(from.getId());
		mplew.writeInt(skillId);

		return mplew.getPacket();
	}

	public static byte[] damagePlayer(int cid, int type, int damage, int monsteridfrom, byte direction, int skillid, int pDMG, boolean pPhysical, int pID, byte pType, Point pPos, byte offset, int offset_d, int fake) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.DAMAGE_PLAYER.getValue());
		mplew.writeInt(cid);
		mplew.write(type);
		mplew.writeInt(damage);
		mplew.write(0);
		if (type >= -1) {
			mplew.writeInt(monsteridfrom);
			mplew.write(direction);
			mplew.writeInt(skillid);
			mplew.writeInt(pDMG);
			mplew.write(0);
			if (pDMG > 0) {
				mplew.write(pPhysical ? 1 : 0);
				mplew.writeInt(pID);
				mplew.write(pType);
				mplew.writePos(pPos);
			}
			mplew.write(offset);
			if (offset == 1) {
				mplew.writeInt(offset_d);
			}
		}
		mplew.writeInt(damage);
		if ((damage <= 0) || (fake > 0)) {
			mplew.writeInt(fake);
		}

		return mplew.getPacket();
	}

	public static byte[] facialExpression(MapleCharacter from, int expression) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.FACIAL_EXPRESSION.getValue());
		mplew.writeInt(from.getId());
		mplew.writeInt(expression);
		mplew.writeInt(-1);
		mplew.write(0);

		return mplew.getPacket();
	}

	public static byte[] itemEffect(int characterid, int itemid) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.SHOW_ITEM_EFFECT.getValue());
		mplew.writeInt(characterid);
		mplew.writeInt(itemid);

		return mplew.getPacket();
	}

	public static byte[] showTitle(int characterid, int itemid) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.SHOW_TITLE.getValue());
		mplew.writeInt(characterid);
		mplew.writeInt(itemid);

		return mplew.getPacket();
	}

	public static byte[] showChair(int characterid, int itemid) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.SHOW_CHAIR.getValue());
		mplew.writeInt(characterid);
		mplew.writeInt(itemid);
		mplew.writeInt(0);

		return mplew.getPacket();
	}

	public static byte[] updateCharLook(MapleCharacter chr) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.UPDATE_CHAR_LOOK.getValue());
		mplew.writeInt(chr.getId());
		mplew.write(1);
		PacketHelper.addCharLook(mplew, chr, false);
		Triple<List<MapleRing>, List<MapleRing>, List<MapleRing>> rings = chr.getRings(false);
		addRingInfo(mplew, rings.getLeft());
		addRingInfo(mplew, rings.getMid());
		addMRingInfo(mplew, rings.getRight(), chr);
		mplew.writeInt(0); // -> charid to follow (4)
		return mplew.getPacket();
	}

	public static byte[] updatePartyMemberHP(int cid, int curhp, int maxhp) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.UPDATE_PARTYMEMBER_HP.getValue());
		mplew.writeInt(cid);
		mplew.writeInt(curhp);
		mplew.writeInt(maxhp);

		return mplew.getPacket();
	}

	public static byte[] loadGuildName(MapleCharacter chr) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.LOAD_GUILD_NAME.getValue());
		mplew.writeInt(chr.getId());
		if (chr.getGuildId() <= 0) {
			mplew.writeShort(0);
		} else {
			MapleGuild gs = World.Guild.getGuild(chr.getGuildId());
			if (gs != null)
				mplew.writeMapleAsciiString(gs.getName());
			else {
				mplew.writeShort(0);
			}
		}

		return mplew.getPacket();
	}

	public static byte[] loadGuildIcon(MapleCharacter chr) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.LOAD_GUILD_ICON.getValue());
		mplew.writeInt(chr.getId());
		if (chr.getGuildId() <= 0) {
			mplew.writeZeroBytes(6);
		} else {
			MapleGuild gs = World.Guild.getGuild(chr.getGuildId());
			if (gs != null) {
				mplew.writeShort(gs.getLogoBG());
				mplew.write(gs.getLogoBGColor());
				mplew.writeShort(gs.getLogo());
				mplew.write(gs.getLogoColor());
			} else {
				mplew.writeZeroBytes(6);
			}
		}

		return mplew.getPacket();
	}

	public static byte[] changeTeam(int cid, int type) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.LOAD_TEAM.getValue());
		mplew.writeInt(cid);
		mplew.write(type);

		return mplew.getPacket();
	}

	public static byte[] showHarvesting(MapleClient c, int tool) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.SHOW_HARVEST.getValue());
		mplew.writeInt(c.getPlayer().getId());
		mplew.writeInt(1);
		mplew.writeInt(tool);
		mplew.writeInt(0);

		return mplew.getPacket();
	}

	public static byte[] getPVPHPBar(int cid, int hp, int maxHp) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.PVP_HP.getValue());
		mplew.writeInt(cid);
		mplew.writeInt(hp);
		mplew.writeInt(maxHp);

		return mplew.getPacket();
	}

	public static byte[] cancelChair(int id) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.CANCEL_CHAIR.getValue());
		if (id == -1) {
			mplew.write(0);
		} else {
			mplew.write(1);
			mplew.writeShort(id);
		}

		return mplew.getPacket();
	}

	public static byte[] instantMapWarp(byte portal) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.CURRENT_MAP_WARP.getValue());
		mplew.write(0);
		mplew.write(portal);

		return mplew.getPacket();
	}

	public static byte[] updateQuestInfo(MapleCharacter c, int quest, int npc, byte progress) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.UPDATE_QUEST_INFO.getValue());
		mplew.write(progress);
		mplew.writeShort(quest);
		mplew.writeInt(npc);
		mplew.writeInt(0);

		return mplew.getPacket();
	}

	public static byte[] updateQuestFinish(int quest, int npc, int nextquest) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.UPDATE_QUEST_INFO.getValue());
		mplew.write(10);
		mplew.writeShort(quest);
		mplew.writeInt(npc);
		mplew.writeInt(nextquest);

		return mplew.getPacket();
	}

	public static byte[] sendHint(String hint, int width, int height) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.PLAYER_HINT.getValue());
		mplew.writeMapleAsciiString(hint);
		mplew.writeShort(width < 1 ? Math.max(hint.length() * 10, 40) : width);
		mplew.writeShort(Math.max(height, 5));
		mplew.write(1);

		return mplew.getPacket();
	}

	public static byte[] testCombo(int value) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.ARAN_COMBO.getValue());
		mplew.writeInt(value);

		return mplew.getPacket();
	}

	public static byte[] rechargeCombo(int value) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.ARAN_COMBO_RECHARGE.getValue());
		mplew.writeInt(value);

		return mplew.getPacket();
	}

	public static byte[] getFollowMessage(String msg) {
		return getGameMessage(msg, true);
	}

	public static byte[] getGameMessage(String msg, boolean white) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.GAME_MESSAGE.getValue());
		mplew.writeShort(white ? 11 : 6);
		mplew.writeMapleAsciiString(msg);

		return mplew.getPacket();
	}

	public static byte[] getBuffZoneEffect(int itemId) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.BUFF_ZONE_EFFECT.getValue());
		mplew.writeInt(itemId);

		return mplew.getPacket();
	}

	public static byte[] getTimeBombAttack() {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.TIME_BOMB_ATTACK.getValue());
		mplew.writeInt(0);
		mplew.writeInt(0);
		mplew.writeInt(0);
		mplew.writeInt(10);
		mplew.writeInt(6);

		return mplew.getPacket();
	}

	public static byte[] moveFollow(Point otherStart, Point myStart, Point otherEnd, List<LifeMovementFragment> moves) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.FOLLOW_MOVE.getValue());
		mplew.writePos(otherStart);
		mplew.writePos(myStart);
		PacketHelper.serializeMovementList(mplew, moves);
		mplew.write(17);
		for (int i = 0; i < 8; i++) {
			mplew.write(0);
		}
		mplew.write(0);
		mplew.writePos(otherEnd);
		mplew.writePos(otherStart);

		return mplew.getPacket();
	}

	public static byte[] getFollowMsg(int opcode) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.FOLLOW_MSG.getValue());
		mplew.writeLong(opcode);

		return mplew.getPacket();
	}

	public static byte[] registerFamiliar(MonsterFamiliar mf) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.REGISTER_FAMILIAR.getValue());
		mplew.writeLong(mf.getId());
		mf.writeRegisterPacket(mplew, false);
		mplew.writeShort(mf.getVitality() >= 3 ? 1 : 0);

		return mplew.getPacket();
	}

	public static byte[] createUltimate(int amount) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.CREATE_ULTIMATE.getValue());
		mplew.writeInt(amount);

		return mplew.getPacket();
	}

	public static byte[] harvestMessage(int oid, int msg) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.HARVEST_MESSAGE.getValue());
		mplew.writeInt(oid);
		mplew.writeInt(msg);

		return mplew.getPacket();
	}

	public static byte[] openBag(int index, int itemId, boolean firstTime) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.OPEN_BAG.getValue());
		mplew.writeInt(index);
		mplew.writeInt(itemId);
		mplew.writeShort(firstTime ? 1 : 0);

		return mplew.getPacket();
	}

	public static byte[] dragonBlink(int portalId) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.DRAGON_BLINK.getValue());
		mplew.write(portalId);

		return mplew.getPacket();
	}

	public static byte[] getPVPIceGage(int score) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.PVP_ICEGAGE.getValue());
		mplew.writeInt(score);

		return mplew.getPacket();
	}

	public static byte[] skillCooldown(int sid, int time) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.COOLDOWN.getValue());
		mplew.writeInt(sid);
		mplew.writeInt(time);

		return mplew.getPacket();
	}

	public static byte[] dropItemFromMapObject(MapleMapItem drop, Point dropfrom, Point dropto, byte mod) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.DROP_ITEM_FROM_MAPOBJECT.getValue());
		mplew.write(mod);
		mplew.writeInt(drop.getObjectId());
		mplew.write(drop.getMeso() > 0 ? 1 : 0);
		mplew.writeInt(drop.getItemId());
		mplew.writeInt(drop.getOwner());
		mplew.write(drop.getDropType());
		mplew.writePos(dropto);
		mplew.writeInt(0);
		if (mod != 2) {
			mplew.writePos(dropfrom);
			mplew.writeShort(0);
		}
		if (drop.getMeso() == 0) {
			PacketHelper.addExpirationTime(mplew, drop.getItem().getExpiration());
		}
		mplew.writeShort(drop.isPlayerDrop() ? 0 : 1);

		return mplew.getPacket();
	}

	public static byte[] explodeDrop(int oid) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.REMOVE_ITEM_FROM_MAP.getValue());
		mplew.write(4);
		mplew.writeInt(oid);
		mplew.writeShort(655);

		return mplew.getPacket();
	}

	public static byte[] removeItemFromMap(int oid, int animation, int cid) {
		return removeItemFromMap(oid, animation, cid, 0);
	}

	public static byte[] removeItemFromMap(int oid, int animation, int cid, int slot) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.REMOVE_ITEM_FROM_MAP.getValue());
		mplew.write(animation);
		mplew.writeInt(oid);
		if (animation >= 2) {
			mplew.writeInt(cid);
			if (animation == 5) {
				mplew.writeInt(slot);
			}
		}
		return mplew.getPacket();
	}

	public static byte[] spawnMist(MapleMist mist) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.SPAWN_MIST.getValue());
		mplew.writeInt(mist.getObjectId());
		mplew.writeInt(mist.isMobMist() ? 0 : mist.isPoisonMist());
		mplew.writeInt(mist.getOwnerId());
		if (mist.getMobSkill() == null)
			mplew.writeInt(mist.getSourceSkill().getId());
		else {
			mplew.writeInt(mist.getMobSkill().getSkillId());
		}
		mplew.write(mist.getSkillLevel());
		mplew.writeShort(mist.getSkillDelay());
		mplew.writeRect(mist.getBox());
		mplew.writeLong(0L);
		mplew.writeInt(0);

		return mplew.getPacket();
	}

	public static byte[] removeMist(int oid, boolean eruption) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.REMOVE_MIST.getValue());
		mplew.writeInt(oid);
		mplew.write(eruption ? 1 : 0);

		return mplew.getPacket();
	}

	public static byte[] spawnDoor(int oid, Point pos, boolean animation) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.SPAWN_DOOR.getValue());
		mplew.write(animation ? 0 : 1);
		mplew.writeInt(oid);
		mplew.writePos(pos);

		return mplew.getPacket();
	}

	public static byte[] removeDoor(int oid, boolean animation) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.REMOVE_DOOR.getValue());
		mplew.write(animation ? 0 : 1);
		mplew.writeInt(oid);

		return mplew.getPacket();
	}

	public static byte[] spawnMechDoor(MechDoor md, boolean animated) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.MECH_DOOR_SPAWN.getValue());
		mplew.write(animated ? 0 : 1);
		mplew.writeInt(md.getOwnerId());
		mplew.writePos(md.getTruePosition());
		mplew.write(md.getId());
		mplew.writeInt(md.getPartyId());
		return mplew.getPacket();
	}

	public static byte[] removeMechDoor(MechDoor md, boolean animated) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.MECH_DOOR_REMOVE.getValue());
		mplew.write(animated ? 0 : 1);
		mplew.writeInt(md.getOwnerId());
		mplew.write(md.getId());

		return mplew.getPacket();
	}

	public static byte[] triggerReactor(MapleReactor reactor, int stance) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.REACTOR_HIT.getValue());
		mplew.writeInt(reactor.getObjectId());
		mplew.write(reactor.getState());
		mplew.writePos(reactor.getTruePosition());
		mplew.writeInt(stance);
		return mplew.getPacket();
	}

	public static byte[] spawnReactor(MapleReactor reactor) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.REACTOR_SPAWN.getValue());
		mplew.writeInt(reactor.getObjectId());
		mplew.writeInt(reactor.getReactorId());
		mplew.write(reactor.getState());
		mplew.writePos(reactor.getTruePosition());
		mplew.write(reactor.getFacingDirection());
		mplew.writeMapleAsciiString(reactor.getName());

		return mplew.getPacket();
	}

	public static byte[] destroyReactor(MapleReactor reactor) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.REACTOR_DESTROY.getValue());
		mplew.writeInt(reactor.getObjectId());
		mplew.write(reactor.getState());
		mplew.writePos(reactor.getPosition());

		return mplew.getPacket();
	}

	public static byte[] makeExtractor(int cid, String cname, Point pos, int timeLeft, int itemId, int fee) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.SPAWN_EXTRACTOR.getValue());
		mplew.writeInt(cid);
		mplew.writeMapleAsciiString(cname);
		mplew.writeInt(pos.x);
		mplew.writeInt(pos.y);
		mplew.writeShort(timeLeft);
		mplew.writeInt(itemId);
		mplew.writeInt(fee);

		return mplew.getPacket();
	}

	public static byte[] removeExtractor(int cid) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.REMOVE_EXTRACTOR.getValue());
		mplew.writeInt(cid);
		mplew.writeInt(1);

		return mplew.getPacket();
	}

	public static byte[] rollSnowball(int type, MapleSnowball.MapleSnowballs ball1, MapleSnowball.MapleSnowballs ball2) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.ROLL_SNOWBALL.getValue());
		mplew.write(type);
		mplew.writeInt(ball1 == null ? 0 : ball1.getSnowmanHP() / 75);
		mplew.writeInt(ball2 == null ? 0 : ball2.getSnowmanHP() / 75);
		mplew.writeShort(ball1 == null ? 0 : ball1.getPosition());
		mplew.write(0);
		mplew.writeShort(ball2 == null ? 0 : ball2.getPosition());
		mplew.writeZeroBytes(11);

		return mplew.getPacket();
	}

	public static byte[] enterSnowBall() {
		return rollSnowball(0, null, null);
	}

	public static byte[] hitSnowBall(int team, int damage, int distance, int delay) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.HIT_SNOWBALL.getValue());
		mplew.write(team);
		mplew.writeShort(damage);
		mplew.write(distance);
		mplew.write(delay);

		return mplew.getPacket();
	}

	public static byte[] snowballMessage(int team, int message) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.SNOWBALL_MESSAGE.getValue());
		mplew.write(team);
		mplew.writeInt(message);

		return mplew.getPacket();
	}

	public static byte[] leftKnockBack() {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.LEFT_KNOCK_BACK.getValue());

		return mplew.getPacket();
	}

	public static byte[] hitCoconut(boolean spawn, int id, int type) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.HIT_COCONUT.getValue());
		mplew.writeInt(spawn ? 32768 : id);
		mplew.write(spawn ? 0 : type);

		return mplew.getPacket();
	}

	public static byte[] coconutScore(int[] coconutscore) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.COCONUT_SCORE.getValue());
		mplew.writeShort(coconutscore[0]);
		mplew.writeShort(coconutscore[1]);

		return mplew.getPacket();
	}

	public static byte[] updateAriantScore(List<MapleCharacter> players) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.ARIANT_SCORE_UPDATE.getValue());
		mplew.write(players.size());
		for (MapleCharacter i : players) {
			mplew.writeMapleAsciiString(i.getName());
			mplew.writeInt(0);
		}

		return mplew.getPacket();
	}

	public static byte[] sheepRanchInfo(byte wolf, byte sheep) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.SHEEP_RANCH_INFO.getValue());
		mplew.write(wolf);
		mplew.write(sheep);

		return mplew.getPacket();
	}

	public static byte[] sheepRanchClothes(int cid, byte clothes) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.SHEEP_RANCH_CLOTHES.getValue());
		mplew.writeInt(cid);
		mplew.write(clothes);

		return mplew.getPacket();
	}

	public static byte[] updateWitchTowerKeys(int keys) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.WITCH_TOWER.getValue());
		mplew.write(keys);

		return mplew.getPacket();
	}

	public static byte[] showChaosZakumShrine(boolean spawned, int time) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.CHAOS_ZAKUM_SHRINE.getValue());
		mplew.write(spawned ? 1 : 0);
		mplew.writeInt(time);

		return mplew.getPacket();
	}

	public static byte[] showChaosHorntailShrine(boolean spawned, int time) {
		return showHorntailShrine(spawned, time);
	}

	public static byte[] showHorntailShrine(boolean spawned, int time) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.HORNTAIL_SHRINE.getValue());
		mplew.write(spawned ? 1 : 0);
		mplew.writeInt(time);

		return mplew.getPacket();
	}

	public static byte[] getRPSMode(byte mode, int mesos, int selection, int answer) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.RPS_GAME.getValue());
		mplew.write(mode);
		switch (mode) {
		case 6:
			if (mesos == -1)
				break;
			mplew.writeInt(mesos);
			break;
		case 8:
			mplew.writeInt(9000019);
			break;
		case 11:
			mplew.write(selection);
			mplew.write(answer);
		}

		return mplew.getPacket();
	}

	public static byte[] messengerInvite(String from, int messengerid) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.MESSENGER.getValue());
		mplew.write(3);
		mplew.writeMapleAsciiString(from);
		mplew.write(0);
		mplew.writeInt(messengerid);
		mplew.write(0);

		return mplew.getPacket();
	}

	public static byte[] addMessengerPlayer(String from, MapleCharacter chr, int position, int channel) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.MESSENGER.getValue());
		mplew.write(0);
		mplew.write(position);
		PacketHelper.addCharLook(mplew, chr, true);
		mplew.writeMapleAsciiString(from);
		mplew.write(channel);
		mplew.write(0);

		return mplew.getPacket();
	}

	public static byte[] removeMessengerPlayer(int position) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.MESSENGER.getValue());
		mplew.write(2);
		mplew.write(position);

		return mplew.getPacket();
	}

	public static byte[] updateMessengerPlayer(String from, MapleCharacter chr, int position, int channel) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.MESSENGER.getValue());
		mplew.write(7);
		mplew.write(position);
		PacketHelper.addCharLook(mplew, chr, true);
		mplew.writeMapleAsciiString(from);
		mplew.writeShort(channel);

		return mplew.getPacket();
	}

	public static byte[] joinMessenger(int position) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.MESSENGER.getValue());
		mplew.write(1);
		mplew.write(position);

		return mplew.getPacket();
	}

	public static byte[] messengerChat(String charname, String text) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.MESSENGER.getValue());
		mplew.write(6);
		mplew.writeMapleAsciiString(charname);
		mplew.writeMapleAsciiString(text);

		return mplew.getPacket();
	}

	public static byte[] messengerNote(String text, int mode, int mode2) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.MESSENGER.getValue());
		mplew.write(mode);
		mplew.writeMapleAsciiString(text);
		mplew.write(mode2);

		return mplew.getPacket();
	}

	public static byte[] removeItemFromDuey(boolean remove, int Package) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.DUEY.getValue());
		mplew.write(24);
		mplew.writeInt(Package);
		mplew.write(remove ? 3 : 4);

		return mplew.getPacket();
	}

	public static byte[] sendDuey(byte operation, List<MapleDueyActions> packages) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.DUEY.getValue());
		mplew.write(operation);

		switch (operation) {
		case 9:
			mplew.write(1);

			break;
		case 10:
			mplew.write(0);
			mplew.write(packages.size());

			for (MapleDueyActions dp : packages) {
				mplew.writeInt(dp.getPackageId());
				mplew.writeAsciiString(dp.getSender(), 13);
				mplew.writeInt(dp.getMesos());
				mplew.writeLong(PacketHelper.getTime(dp.getSentTime()));
				mplew.writeZeroBytes(205);

				if (dp.getItem() != null) {
					mplew.write(1);
					PacketHelper.addItemInfo(mplew, dp.getItem());
				} else {
					mplew.write(0);
				}
			}

			mplew.write(0);
		}

		return mplew.getPacket();
	}

	public static byte[] getKeymap(MapleKeyLayout layout) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.KEYMAP.getValue());

		layout.writeData(mplew);

		return mplew.getPacket();
	}

	public static byte[] petAutoHP(int itemId) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.PET_AUTO_HP.getValue());
		mplew.writeInt(itemId);

		return mplew.getPacket();
	}

	public static byte[] petAutoMP(int itemId) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.PET_AUTO_MP.getValue());
		mplew.writeInt(itemId);

		return mplew.getPacket();
	}

	public static void addRingInfo(MaplePacketLittleEndianWriter mplew, List<MapleRing> rings) {
		mplew.write(rings.size());
		for (MapleRing ring : rings) {
			mplew.writeInt(1);
			mplew.writeLong(ring.getRingId());
			mplew.writeLong(ring.getPartnerRingId());
			mplew.writeInt(ring.getItemId());
		}
	}

	public static void addMRingInfo(MaplePacketLittleEndianWriter mplew, List<MapleRing> rings, MapleCharacter chr) {
		mplew.write(rings.size());
		for (MapleRing ring : rings) {
			mplew.writeInt(1);
			mplew.writeInt(chr.getId());
			mplew.writeInt(ring.getPartnerChrId());
			mplew.writeInt(ring.getItemId());
		}
	}

	public static byte[] getBuffBar(long millis) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.BUFF_BAR.getValue());
		mplew.writeLong(millis);

		return mplew.getPacket();
	}

	public static byte[] getBoosterFamiliar(int cid, int familiar, int id) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.BOOSTER_FAMILIAR.getValue());
		mplew.writeInt(cid);
		mplew.writeInt(familiar);
		mplew.writeLong(id);
		mplew.write(0);

		return mplew.getPacket();
	}

	static {
		DEFAULT_BUFFMASK |= MapleBuffStat.ENERGY_CHARGE.getValue();
		DEFAULT_BUFFMASK |= MapleBuffStat.DASH_SPEED.getValue();
		DEFAULT_BUFFMASK |= MapleBuffStat.DASH_JUMP.getValue();
		DEFAULT_BUFFMASK |= MapleBuffStat.MONSTER_RIDING.getValue();
		DEFAULT_BUFFMASK |= MapleBuffStat.SPEED_INFUSION.getValue();
		DEFAULT_BUFFMASK |= MapleBuffStat.HOMING_BEACON.getValue();
		DEFAULT_BUFFMASK |= MapleBuffStat.DEFAULT_BUFFSTAT.getValue();
	}

	public static byte[] updateCardStack(int total) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.PHANTOM_CARD.getValue());
		mplew.write(total);

		return mplew.getPacket();
	}

	public static byte[] viewSkills(MapleCharacter chr) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.TARGET_SKILL.getValue());
		List<Integer> skillz = new ArrayList<Integer>();
		for (Skill sk : chr.getSkills().keySet()) {
			if ((sk.canBeLearnedBy(chr.getJob())) && (GameConstants.canSteal(sk)) && (!skillz.contains(sk.getId()))) {
				skillz.add(sk.getId());
			}
		}
		mplew.write(1);
		mplew.writeInt(chr.getId());
		mplew.writeInt(skillz.isEmpty() ? 2 : 4);
		mplew.writeInt(chr.getJob());
		mplew.writeInt(skillz.size());
		for (Iterator<Integer> i$ = skillz.iterator(); i$.hasNext();) {
			int i = i$.next().intValue();
			mplew.writeInt(i);
		}

		return mplew.getPacket();
	}

	public static class InteractionPacket {
		public static byte[] getTradeInvite(MapleCharacter c) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
			mplew.write(PlayerInteractionHandler.Interaction.INVITE_TRADE.action);
			mplew.write(3);
			mplew.writeMapleAsciiString(c.getName());
			mplew.writeInt(c.getLevel());
			mplew.writeInt(c.getJob());
			return mplew.getPacket();
		}

		public static byte[] getTradeMesoSet(byte number, int meso) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
			mplew.write(PlayerInteractionHandler.Interaction.SET_MESO.action);
			mplew.write(number);
			mplew.writeInt(meso);

			return mplew.getPacket();
		}

		public static byte[] getTradeItemAdd(byte number, Item item) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
			mplew.write(PlayerInteractionHandler.Interaction.SET_ITEMS.action);
			mplew.write(number);
			mplew.write(item.getPosition());
			PacketHelper.addItemInfo(mplew, item);

			return mplew.getPacket();
		}

		public static byte[] getTradeStart(MapleClient c, MapleTrade trade, byte number) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
			mplew.write(10);
			mplew.write(3);
			mplew.write(2);
			mplew.write(number);

			if (number == 1) {
				mplew.write(0);
				PacketHelper.addCharLook(mplew, trade.getPartner().getChr(), false);
				mplew.writeMapleAsciiString(trade.getPartner().getChr().getName());
				mplew.writeShort(trade.getPartner().getChr().getJob());
			}
			mplew.write(number);
			PacketHelper.addCharLook(mplew, c.getPlayer(), false);
			mplew.writeMapleAsciiString(c.getPlayer().getName());
			mplew.writeShort(c.getPlayer().getJob());
			mplew.write(255);

			return mplew.getPacket();
		}

		public static byte[] getTradeConfirmation() {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
			mplew.write(PlayerInteractionHandler.Interaction.CONFIRM_TRADE.action);

			return mplew.getPacket();
		}

		public static byte[] TradeMessage(byte UserSlot, byte message) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
			mplew.write(GameConstants.GMS ? 18 : 10);
			mplew.write(UserSlot);
			mplew.write(message);

			return mplew.getPacket();
		}

		public static byte[] getTradeCancel(byte UserSlot, int unsuccessful) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
			mplew.write(PlayerInteractionHandler.Interaction.EXIT.action);
			mplew.write(UserSlot);
			mplew.write(2);

			return mplew.getPacket();
		}

	}

	public static class NPCPacket {
		public static byte[] spawnNPC(MapleNPC life, boolean show) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.SPAWN_NPC.getValue());
			mplew.writeInt(life.getObjectId());
			mplew.writeInt(life.getId());
			mplew.writeShort(life.getPosition().x);
			mplew.writeShort(life.getCy());
			mplew.write(life.getF() == 1 ? 0 : 1);
			mplew.writeShort(life.getFh());
			mplew.writeShort(life.getRx0());
			mplew.writeShort(life.getRx1());
			mplew.write(show ? 1 : 0);

			return mplew.getPacket();
		}

		public static byte[] removeNPC(int objectid) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.REMOVE_NPC.getValue());
			mplew.writeInt(objectid);

			return mplew.getPacket();
		}

		public static byte[] removeNPCController(int objectid) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.SPAWN_NPC_REQUEST_CONTROLLER.getValue());
			mplew.write(0);
			mplew.writeInt(objectid);

			return mplew.getPacket();
		}

		public static byte[] spawnNPCRequestController(MapleNPC life, boolean MiniMap) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.SPAWN_NPC_REQUEST_CONTROLLER.getValue());
			mplew.write(1);
			mplew.writeInt(life.getObjectId());
			mplew.writeInt(life.getId());
			mplew.writeShort(life.getPosition().x);
			mplew.writeShort(life.getCy());
			mplew.write(life.getF() == 1 ? 0 : 1);
			mplew.writeShort(life.getFh());
			mplew.writeShort(life.getRx0());
			mplew.writeShort(life.getRx1());
			mplew.write(MiniMap ? 1 : 0);

			return mplew.getPacket();
		}

		public static byte[] setNPCScriptable(List<Pair<Integer, String>> npcs) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
			mplew.writeShort(SendPacketOpcode.NPC_SCRIPTABLE.getValue());
			mplew.write(npcs.size());
			for (Pair<?, ?> s : npcs) {
				mplew.writeInt(((Integer) s.left).intValue());
				mplew.writeMapleAsciiString((String) s.right);
				mplew.writeInt(0);
				mplew.writeInt(2147483647);
			}
			return mplew.getPacket();
		}

		public static byte[] getNPCTalk(int npc, byte msgType, String talk, String endBytes, byte type) {
			return getNPCTalk(npc, msgType, talk, endBytes, type, npc);
		}

		public static byte[] getNPCTalk(int npc, byte msgType, String talk, String endBytes, byte type, int diffNPC) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
			mplew.write(4);
			mplew.writeInt(npc);
			mplew.write(msgType);
			mplew.write(type); // mask; 1 = no ESC, 2 = playerspeaks, 4 = diff NPC 8 = something, ty KDMS
			if ((type & 0x4) != 0) {
				mplew.writeInt(diffNPC);
			}
			mplew.writeMapleAsciiString(talk);
			mplew.write(HexTool.getByteArrayFromHexString(endBytes));

			return mplew.getPacket();
		}

		public static byte[] getMapSelection(int npcid, String sel) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
			mplew.write(4);
			mplew.writeInt(npcid);
			mplew.writeShort(GameConstants.GMS ? 17 : 16);
			mplew.writeInt(npcid == 2083006 ? 1 : 0);
			mplew.writeInt(npcid == 9010022 ? 1 : 0);
			mplew.writeMapleAsciiString(sel);

			return mplew.getPacket();
		}

		public static byte[] getNPCTalkStyle(int npc, String talk, int[] args) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
			mplew.write(4);
			mplew.writeInt(npc);
			mplew.writeShort(9);
			mplew.writeMapleAsciiString(talk);
			mplew.write(args.length);

			for (int i = 0; i < args.length; i++) {
				mplew.writeInt(args[i]);
			}
			return mplew.getPacket();
		}

		public static byte[] getNPCTalkNum(int npc, String talk, int def, int min, int max) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
			mplew.write(4);
			mplew.writeInt(npc);
			mplew.writeShort(4);
			mplew.writeMapleAsciiString(talk);
			mplew.writeInt(def);
			mplew.writeInt(min);
			mplew.writeInt(max);
			mplew.writeInt(0);

			return mplew.getPacket();
		}

		public static byte[] getNPCTalkText(int npc, String talk) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
			mplew.write(4);
			mplew.writeInt(npc);
			mplew.writeShort(3);
			mplew.writeMapleAsciiString(talk);
			mplew.writeInt(0);
			mplew.writeInt(0);

			return mplew.getPacket();
		}

		public static byte[] getSelfTalkText(String text) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
			mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
			mplew.write(3);
			mplew.writeInt(0);
			mplew.write(0);
			mplew.write(17);
			mplew.writeMapleAsciiString(text);
			return mplew.getPacket();
		}

		public static byte[] getNPCTutoEffect(String effect) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
			mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
			mplew.write(3);
			mplew.writeInt(0);
			mplew.write(0);
			mplew.write(1);
			mplew.writeShort(257);
			mplew.writeMapleAsciiString(effect);
			return mplew.getPacket();
		}

		public static byte[] getEvanTutorial(String data) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());

			mplew.write(8);
			mplew.writeInt(0);
			mplew.write(1);
			mplew.write(1);
			mplew.write(1);
			mplew.writeMapleAsciiString(data);

			return mplew.getPacket();
		}

		public static byte[] getNPCShop(int sid, MapleShop shop, MapleClient c) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.OPEN_NPC_SHOP.getValue());
			mplew.writeInt(0);
			mplew.writeInt(sid);
			PacketHelper.addShopInfo(mplew, shop, c);

			return mplew.getPacket();
		}

		public static byte[] confirmShopTransaction(byte code, MapleShop shop, MapleClient c, int indexBought) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.CONFIRM_SHOP_TRANSACTION.getValue());
			mplew.write(code);
			if (code == 4) {
				mplew.writeInt(0);
				mplew.writeInt(shop.getNpcId());
				PacketHelper.addShopInfo(mplew, shop, c);
			} else {
				mplew.write(indexBought >= 0 ? 1 : 0);
				if (indexBought >= 0) {
					mplew.writeInt(indexBought);
				}
			}

			return mplew.getPacket();
		}

		public static byte[] getStorage(int npcId, byte slots, Collection<Item> items, int meso) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.OPEN_STORAGE.getValue());
			mplew.write(22);
			mplew.writeInt(npcId);
			mplew.write(slots);
			mplew.writeShort(126);
			mplew.writeShort(0);
			mplew.writeInt(0);
			mplew.writeInt(meso);
			mplew.writeShort(0);
			mplew.write((byte) items.size());
			for (Item item : items) {
				PacketHelper.addItemInfo(mplew, item);
			}
			mplew.writeShort(0);
			mplew.write(0);

			return mplew.getPacket();
		}

		public static byte[] getStorageFull() {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.OPEN_STORAGE.getValue());
			mplew.write(17);

			return mplew.getPacket();
		}

		public static byte[] mesoStorage(byte slots, int meso) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.OPEN_STORAGE.getValue());
			mplew.write(19);
			mplew.write(slots);
			mplew.writeShort(2);
			mplew.writeShort(0);
			mplew.writeInt(0);
			mplew.writeInt(meso);

			return mplew.getPacket();
		}

		public static byte[] arrangeStorage(byte slots, Collection<Item> items, boolean changed) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.OPEN_STORAGE.getValue());
			mplew.write(15);
			mplew.write(slots);
			mplew.write(124);
			mplew.writeZeroBytes(10);
			mplew.write(items.size());
			for (Item item : items) {
				PacketHelper.addItemInfo(mplew, item);
			}
			mplew.write(0);
			return mplew.getPacket();
		}

		public static byte[] storeStorage(byte slots, MapleInventoryType type, Collection<Item> items) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.OPEN_STORAGE.getValue());
			mplew.write(13);
			mplew.write(slots);
			mplew.writeShort(type.getBitfieldEncoding());
			mplew.writeShort(0);
			mplew.writeInt(0);
			mplew.write(items.size());
			for (Item item : items) {
				PacketHelper.addItemInfo(mplew, item);
			}
			return mplew.getPacket();
		}

		public static byte[] takeOutStorage(byte slots, MapleInventoryType type, Collection<Item> items) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.OPEN_STORAGE.getValue());
			mplew.write(9);
			mplew.write(slots);
			mplew.writeShort(type.getBitfieldEncoding());
			mplew.writeShort(0);
			mplew.writeInt(0);
			mplew.write(items.size());
			for (Item item : items) {
				PacketHelper.addItemInfo(mplew, item);
			}
			return mplew.getPacket();
		}

	}

	public static class SummonPacket {
		public static byte[] spawnSummon(MapleSummon summon, boolean animated) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.SPAWN_SUMMON.getValue());
			mplew.writeInt(summon.getOwnerId());
			mplew.writeInt(summon.getObjectId());
			mplew.writeInt(summon.getSkill());
			mplew.write(summon.getOwnerLevel() - 1);
			mplew.write(summon.getSkillLevel());
			mplew.writePos(summon.getPosition());
			mplew.write((summon.getSkill() == 32111006) || (summon.getSkill() == 33101005) ? 5 : 4);
			if ((summon.getSkill() == 35121003) && (summon.getOwner().getMap() != null))
				mplew.writeShort(summon.getOwner().getMap().getFootholds().findBelow(summon.getPosition()).getId());
			else {
				mplew.writeShort(0);
			}
			mplew.write(summon.getMovementType().getValue());
			mplew.write(summon.getSummonType());
			mplew.write(animated ? 1 : 0);
			mplew.write(1);
			MapleCharacter chr = summon.getOwner();
			mplew.write((summon.getSkill() == 4341006) && (chr != null) ? 1 : 0);
			if ((summon.getSkill() == 4341006) && (chr != null)) {
				PacketHelper.addCharLook(mplew, chr, true);
			}
			if (summon.getSkill() == 35111002) {
				mplew.write(0);
			}

			return mplew.getPacket();
		}

		public static byte[] removeSummon(int ownerId, int objId) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.REMOVE_SUMMON.getValue());
			mplew.writeInt(ownerId);
			mplew.writeInt(objId);
			mplew.write(10);

			return mplew.getPacket();
		}

		public static byte[] removeSummon(MapleSummon summon, boolean animated) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.REMOVE_SUMMON.getValue());
			mplew.writeInt(summon.getOwnerId());
			mplew.writeInt(summon.getObjectId());
			if (animated)
				switch (summon.getSkill()) {
				case 35121003:
					mplew.write(10);
					break;
				case 33101008:

				case 35111001:

				case 35111002:

				case 35111005:

				case 35111009:

				case 35111010:

				case 35111011:

				case 35121009:

				case 35121010:

				case 35121011:
					mplew.write(5);
					break;
				default:
					mplew.write(4);
					break;
				}
			else {
				mplew.write(1);
			}

			return mplew.getPacket();
		}

		public static byte[] moveSummon(int cid, int oid, Point startPos, List<LifeMovementFragment> moves) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.MOVE_SUMMON.getValue());
			mplew.writeInt(cid);
			mplew.writeInt(oid);
			mplew.writePos(startPos);
			mplew.writeInt(0);
			PacketHelper.serializeMovementList(mplew, moves);

			return mplew.getPacket();
		}

		public static byte[] summonAttack(int cid, int summonSkillId, byte animation, List<Pair<Integer, Integer>> allDamage, int level, boolean darkFlare) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.SUMMON_ATTACK.getValue());
			mplew.writeInt(cid);
			mplew.writeInt(summonSkillId);
			mplew.write(level - 1);
			mplew.write(animation);
			mplew.write(allDamage.size());
			for (Pair<?, ?> attackEntry : allDamage) {
				mplew.writeInt(((Integer) attackEntry.left).intValue());
				mplew.write(7);
				mplew.writeInt(((Integer) attackEntry.right).intValue());
			}
			mplew.write(darkFlare ? 1 : 0);

			return mplew.getPacket();
		}

		public static byte[] pvpSummonAttack(int cid, int playerLevel, int oid, int animation, Point pos, List<AttackPair> attack) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.PVP_SUMMON.getValue());
			mplew.writeInt(cid);
			mplew.writeInt(oid);
			mplew.write(playerLevel);
			mplew.write(animation);
			mplew.writePos(pos);
			mplew.writeInt(0);
			mplew.write(attack.size());
			for (AttackPair p : attack) {
				mplew.writeInt(p.objectid);
				mplew.writePos(p.point);
				mplew.write(p.attack.size());
				mplew.write(0);
				for (Pair<?, ?> atk : p.attack) {
					mplew.writeInt(((Integer) atk.left).intValue());
				}
			}

			return mplew.getPacket();
		}

		public static byte[] summonSkill(int cid, int summonSkillId, int newStance) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.SUMMON_SKILL.getValue());
			mplew.writeInt(cid);
			mplew.writeInt(summonSkillId);
			mplew.write(newStance);

			return mplew.getPacket();
		}

		public static byte[] damageSummon(int cid, int summonSkillId, int damage, int unkByte, int monsterIdFrom) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.DAMAGE_SUMMON.getValue());
			mplew.writeInt(cid);
			mplew.writeInt(summonSkillId);
			mplew.write(unkByte);
			mplew.writeInt(damage);
			mplew.writeInt(monsterIdFrom);
			mplew.write(0);

			return mplew.getPacket();
		}

	}

	public static class UIPacket {
		// public static byte[] getDirectionStatus(boolean enable)
		// {
		// MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		//
		// mplew.writeShort(SendPacketOpcode.DIRECTION_STATUS.getValue());
		// mplew.write(enable ? 1 : 0);
		//
		// return mplew.getPacket();
		// }

		public static byte[] getDirectionStatus(boolean enable) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
			mplew.writeShort(SendPacketOpcode.DIRECTION_STATUS.getValue());
			mplew.write(enable ? 1 : 0);

			return mplew.getPacket();
		}

		public static byte[] IntroEnableUI(int wtf) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.CYGNUS_INTRO_ENABLE_UI.getValue());
			mplew.write(wtf > 0 ? 1 : 0);
			if (wtf > 0) {
				mplew.writeShort(wtf);
			}
			return mplew.getPacket();
		}

		public static byte[] openUI(int type) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(3);

			mplew.writeShort(SendPacketOpcode.OPEN_UI.getValue());
			mplew.write(type);

			return mplew.getPacket();
		}

		public static byte[] sendRepairWindow(int npc) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(10);

			mplew.writeShort(SendPacketOpcode.OPEN_UI_OPTION.getValue());
			mplew.writeInt(33);
			mplew.writeInt(npc);

			return mplew.getPacket();
		}

		public static byte[] startAzwan(int npc) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(10);
			mplew.writeShort(SendPacketOpcode.ENTER_AZWAN.getValue());
			mplew.write(HexTool.getByteArrayFromHexString("01 61 63 FF 01 01 7F 96 98 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 C0 27 00 00 00 00 00 88 00 00 00 00 00 00 00 00 47 80 00 00 00 00 00 00 00 00 47 80 00 00 00 00 00 00 00 00 47 80 00 00 00 00 00 00 00 00 47 80 00 00 00 00 00 4A 0A 84 00 04 08 00 08 00 FF FF 0F 00 00 00 00 00 00 00 00 00 00 00 FF"));
			return mplew.getPacket();
		}

		public static byte[] sendRedLeaf(int npc) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(10);

			mplew.writeShort(SendPacketOpcode.OPEN_UI_OPTION.getValue());
			mplew.writeInt(66); // 70 azwan

			mplew.writeInt(npc);

			return mplew.getPacket();
		}

		public static byte[] IntroLock(boolean enable) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.CYGNUS_INTRO_LOCK.getValue());
			mplew.write(enable ? 1 : 0);
			mplew.writeInt(0);

			return mplew.getPacket();
		}

		// public static byte[] IntroEnableUI(int wtf) {
		// MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		//
		// mplew.writeShort(SendPacketOpcode.CYGNUS_INTRO_ENABLE_UI.getValue());
		// mplew.write(wtf > 0 ? 1 : 0);
		// if (wtf > 0) {
		// mplew.writeShort(wtf);
		// }
		//
		// return mplew.getPacket();
		// }

		public static byte[] IntroDisableUI(boolean enable) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.CYGNUS_INTRO_DISABLE_UI.getValue());
			mplew.write(enable ? 1 : 0);

			return mplew.getPacket();
		}

		public static byte[] summonHelper(boolean summon) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.SUMMON_HINT.getValue());
			mplew.write(summon ? 1 : 0);

			return mplew.getPacket();
		}

		public static byte[] summonMessage(int type) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.SUMMON_HINT_MSG.getValue());
			mplew.write(1);
			mplew.writeInt(type);
			mplew.writeInt(7000);

			return mplew.getPacket();
		}

		public static byte[] summonMessage(String message) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.SUMMON_HINT_MSG.getValue());
			mplew.write(0);
			mplew.writeMapleAsciiString(message);
			mplew.writeInt(200);
			mplew.writeShort(0);
			mplew.writeInt(10000);

			return mplew.getPacket();
		}

		public static byte[] getDirectionInfoTest(byte type, int value) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
			mplew.writeShort(SendPacketOpcode.DIRECTION_INFO.getValue());
			mplew.write(type);
			mplew.writeInt(value);
			return mplew.getPacket();
		}

		public static byte[] getDirectionInfo(int type, int value) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.DIRECTION_INFO.getValue());
			mplew.write(type);
			mplew.writeLong(value);

			return mplew.getPacket();
		}

		public static byte[] getDirectionInfo(String data, int value, int x, int y, int pro) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.DIRECTION_INFO.getValue());
			mplew.write(2);
			mplew.writeMapleAsciiString(data);
			mplew.writeInt(value);
			mplew.writeInt(x);
			mplew.writeInt(y);
			mplew.writeShort(pro);
			if (pro > 0) {
				mplew.writeInt(0);
			}

			return mplew.getPacket();
		}

		public static byte[] reissueMedal(int itemId, int type) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.REISSUE_MEDAL.getValue());
			mplew.write(type);
			mplew.writeInt(itemId);

			return mplew.getPacket();
		}

		public static final byte[] playMovie(String data, boolean show) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.PLAY_MOVIE.getValue());
			mplew.writeMapleAsciiString(data);
			mplew.write(show ? 1 : 0);

			return mplew.getPacket();
		}

	}

	public static class EffectPacket {
		public static byte[] showForeignEffect(int effect) {
			return showForeignEffect(-1, effect);
		}

		public static byte[] showForeignEffect(int cid, int effect) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			if (cid == -1) {
				mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
			} else {
				mplew.writeShort(SendPacketOpcode.SHOW_FOREIGN_EFFECT.getValue());
				mplew.writeInt(cid);
			}
			mplew.write(effect);

			return mplew.getPacket();
		}

		public static byte[] showItemLevelupEffect() {
			return showForeignEffect(18);
		}

		public static byte[] showForeignItemLevelupEffect(int cid) {
			return showForeignEffect(cid, 18);
		}

		public static byte[] showOwnDiceEffect(int skillid, int effectid, int effectid2, int level) {
			return showDiceEffect(-1, skillid, effectid, effectid2, level);
		}

		public static byte[] showDiceEffect(int cid, int skillid, int effectid, int effectid2, int level) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			if (cid == -1) {
				mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
			} else {
				mplew.writeShort(SendPacketOpcode.SHOW_FOREIGN_EFFECT.getValue());
				mplew.writeInt(cid);
			}
			mplew.write(3);
			mplew.writeInt(effectid);
			mplew.writeInt(effectid2);
			mplew.writeInt(skillid);
			mplew.write(level);
			mplew.write(0);

			return mplew.getPacket();
		}

		public static byte[] useCharm(byte charmsleft, byte daysleft, boolean safetyCharm) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
			mplew.write(8);
			mplew.write(safetyCharm ? 1 : 0);
			mplew.write(charmsleft);
			mplew.write(daysleft);
			if (!safetyCharm) {
				mplew.writeInt(0);
			}

			return mplew.getPacket();
		}

		public static byte[] Mulung_DojoUp2() {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
			mplew.write(10);

			return mplew.getPacket();
		}

		public static byte[] showOwnHpHealed(int amount) {
			return showHpHealed(-1, amount);
		}

		public static byte[] showHpHealed(int cid, int amount) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			if (cid == -1) {
				mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
			} else {
				mplew.writeShort(SendPacketOpcode.SHOW_FOREIGN_EFFECT.getValue());
				mplew.writeInt(cid);
			}
			mplew.write(30);
			mplew.writeInt(amount);

			return mplew.getPacket();
		}

		public static byte[] showRewardItemAnimation(int itemId, String effect) {
			return showRewardItemAnimation(itemId, effect, -1);
		}

		public static byte[] showRewardItemAnimation(int itemId, String effect, int from_playerid) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			if (from_playerid == -1) {
				mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
			} else {
				mplew.writeShort(SendPacketOpcode.SHOW_FOREIGN_EFFECT.getValue());
				mplew.writeInt(from_playerid);
			}
			mplew.write(17);
			mplew.writeInt(itemId);
			mplew.write((effect != null) && (effect.length() > 0) ? 1 : 0);
			if ((effect != null) && (effect.length() > 0)) {
				mplew.writeMapleAsciiString(effect);
			}

			return mplew.getPacket();
		}

		public static byte[] showCashItemEffect(int itemId) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
			mplew.write(23);
			mplew.writeInt(itemId);

			return mplew.getPacket();
		}

		public static byte[] ItemMaker_Success() {
			return ItemMaker_Success_3rdParty(-1);
		}

		public static byte[] ItemMaker_Success_3rdParty(int from_playerid) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			if (from_playerid == -1) {
				mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
			} else {
				mplew.writeShort(SendPacketOpcode.SHOW_FOREIGN_EFFECT.getValue());
				mplew.writeInt(from_playerid);
			}
			mplew.write(19);
			mplew.writeInt(0);

			return mplew.getPacket();
		}

		public static byte[] useWheel(byte charmsleft) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
			mplew.write(24);
			mplew.write(charmsleft);

			return mplew.getPacket();
		}

		public static byte[] showOwnBuffEffect(int skillid, int effectid, int playerLevel, int skillLevel) {
			return showBuffeffect(-1, skillid, effectid, playerLevel, skillLevel, (byte) 3);
		}

		public static byte[] showOwnBuffEffect(int skillid, int effectid, int playerLevel, int skillLevel, byte direction) {
			return showBuffeffect(-1, skillid, effectid, playerLevel, skillLevel, direction);
		}

		public static byte[] showBuffeffect(int cid, int skillid, int effectid, int playerLevel, int skillLevel) {
			return showBuffeffect(cid, skillid, effectid, playerLevel, skillLevel, (byte) 3);
		}

		public static byte[] showBuffeffect(int cid, int skillid, int effectid, int playerLevel, int skillLevel, byte direction) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			if (cid == -1) {
				mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
			} else {
				mplew.writeShort(SendPacketOpcode.SHOW_FOREIGN_EFFECT.getValue());
				mplew.writeInt(cid);
			}
			mplew.write(effectid);
			mplew.writeInt(skillid);
			mplew.write(playerLevel - 1);
			if ((effectid == 2) && (skillid == 31111003)) {
				mplew.writeInt(0);
			}
			mplew.write(skillLevel);
			if ((direction != 3) || (skillid == 1320006) || (skillid == 30001062) || (skillid == 30001061)) {
				mplew.write(direction);
			}

			if (skillid == 30001062) {
				mplew.writeInt(0);
			}
			mplew.writeZeroBytes(10);

			return mplew.getPacket();
		}

		public static byte[] ShowWZEffect(String data) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
			mplew.write(21);
			mplew.writeMapleAsciiString(data);

			return mplew.getPacket();
		}

		public static byte[] showOwnCraftingEffect(String effect, int time, int mode) {
			return showCraftingEffect(-1, effect, time, mode);
		}

		public static byte[] showCraftingEffect(int cid, String effect, int time, int mode) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			if (cid == -1) {
				mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
			} else {
				mplew.writeShort(SendPacketOpcode.SHOW_FOREIGN_EFFECT.getValue());
				mplew.writeInt(cid);
			}
			mplew.write(32);
			mplew.writeMapleAsciiString(effect);
			mplew.write(0);
			mplew.writeInt(time);
			mplew.writeInt(mode);
			if (mode == 2) {
				mplew.writeInt(0);
			}

			return mplew.getPacket();
		}

		public static byte[] AranTutInstructionalBalloon(String data) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
			mplew.write(26);
			mplew.writeMapleAsciiString(data);
			mplew.writeInt(1);

			return mplew.getPacket();
		}

		public static byte[] showOwnPetLevelUp(byte index) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
			mplew.write(6);
			mplew.write(0);
			mplew.write(index);

			return mplew.getPacket();
		}

		public static byte[] showOwnChampionEffect() {
			return showChampionEffect(-1);
		}

		public static byte[] showChampionEffect(int from_playerid) {
			MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

			if (from_playerid == -1) {
				mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
			} else {
				mplew.writeShort(SendPacketOpcode.SHOW_FOREIGN_EFFECT.getValue());
				mplew.writeInt(from_playerid);
			}
			mplew.write(34);
			mplew.writeInt(30000);

			return mplew.getPacket();
		}

	}

}

/*
 * Location: C:\Users\Sjögren\Desktop\lithium.jar Qualified Name: tools.packet.CField JD-Core Version: 0.6.0
 */