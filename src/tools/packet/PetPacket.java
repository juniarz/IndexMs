package tools.packet;

import handling.SendPacketOpcode;

import java.util.List;

import server.movement.LifeMovementFragment;
import tools.data.MaplePacketLittleEndianWriter;
import client.MapleCharacter;
import client.MapleStat;
import client.inventory.Item;
import client.inventory.MaplePet;
import constants.GameConstants;

public class PetPacket {
	public static final byte[] updatePet(MaplePet pet, Item item, boolean active) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
		mplew.write(0);
		mplew.write(2);
		mplew.write(3);
		mplew.write(5);
		mplew.writeShort(pet.getInventoryPosition());
		mplew.write(0);
		mplew.write(5);
		mplew.writeShort(pet.getInventoryPosition());
		mplew.write(3);
		mplew.writeInt(pet.getPetItemId());
		mplew.write(1);
		mplew.writeLong(pet.getUniqueId());
		PacketHelper.addPetItemInfo(mplew, item, pet, active);
		return mplew.getPacket();
	}

	public static final byte[] showPet(MapleCharacter chr, MaplePet pet, boolean remove, boolean hunger) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.SPAWN_PET.getValue());
		mplew.writeInt(chr.getId());
		mplew.write(chr.getPetIndex(pet));
		if (remove) {
			mplew.write(0);
			mplew.write(hunger ? 1 : 0);
		} else {
			mplew.write(1);
			mplew.write(0);
			mplew.writeInt(pet.getPetItemId());
			mplew.writeMapleAsciiString(pet.getName());
			mplew.writeLong(pet.getUniqueId());
			mplew.writeShort(pet.getPos().x);
			mplew.writeShort(pet.getPos().y - 20);
			mplew.write(pet.getStance());
			mplew.writeInt(pet.getFh());
		}

		return mplew.getPacket();
	}

	public static final byte[] removePet(int cid, int index) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.SPAWN_PET.getValue());
		mplew.writeInt(cid);
		mplew.write(index);
		mplew.writeShort(0);

		return mplew.getPacket();
	}

	public static final byte[] movePet(int cid, int pid, byte slot, List<LifeMovementFragment> moves) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.MOVE_PET.getValue());
		mplew.writeInt(cid);
		mplew.write(slot);
		mplew.writeLong(pid);
		PacketHelper.serializeMovementList(mplew, moves);

		return mplew.getPacket();
	}

	public static final byte[] petChat(int cid, int un, String text, byte slot) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.PET_CHAT.getValue());
		mplew.writeInt(cid);
		mplew.write(slot);
		mplew.write(un);
		mplew.write(0);
		mplew.writeMapleAsciiString(text);
		mplew.write(0);

		return mplew.getPacket();
	}

	public static final byte[] commandResponse(int cid, byte command, byte slot, boolean success, boolean food) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.PET_COMMAND.getValue());
		mplew.writeInt(cid);
		mplew.write(slot);
		mplew.write(command == 1 ? 1 : 0);
		mplew.write(command);
		mplew.write(success ? 1 : command == 1 ? 0 : 0);
		mplew.write(0);

		return mplew.getPacket();
	}

	public static final byte[] showPetLevelUp(MapleCharacter chr, byte index) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.SHOW_FOREIGN_EFFECT.getValue());
		mplew.writeInt(chr.getId());
		mplew.write(6);
		mplew.write(0);
		mplew.writeInt(index);

		return mplew.getPacket();
	}

	public static final byte[] showPetUpdate(MapleCharacter chr, int uniqueId, byte index) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.PET_EXCEPTION_LIST.getValue());
		mplew.writeInt(chr.getId());
		mplew.write(index);
		mplew.writeLong(uniqueId);
		mplew.write(0);

		return mplew.getPacket();
	}

	public static final byte[] petStatUpdate(MapleCharacter chr) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

		mplew.writeShort(SendPacketOpcode.UPDATE_STATS.getValue());
		mplew.write(0);
		if (GameConstants.GMS)
			mplew.writeLong(MapleStat.PET.getValue());
		else {
			mplew.writeInt((int) MapleStat.PET.getValue());
		}

		byte count = 0;
		for (MaplePet pet : chr.getPets()) {
			if (pet.getSummoned()) {
				mplew.writeLong(pet.getUniqueId());
				count = (byte) (count + 1);
			}
		}
		while (count < 3) {
			mplew.writeZeroBytes(8);
			count = (byte) (count + 1);
		}
		mplew.write(0);
		mplew.writeShort(0);

		return mplew.getPacket();
	}

}

/*
 * Location: C:\Users\Sjögren\Desktop\lithium.jar Qualified Name: tools.packet.PetPacket JD-Core Version: 0.6.0
 */