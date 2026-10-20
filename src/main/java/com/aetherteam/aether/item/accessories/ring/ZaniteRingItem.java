package com.aetherteam.aether.item.accessories.ring;

import com.aetherteam.aether.AetherTags;
import com.aetherteam.aether.client.AetherSoundEvents;
import com.aetherteam.aether.item.accessories.abilities.ZaniteAccessory;

public class ZaniteRingItem extends RingItem implements ZaniteAccessory {
    public ZaniteRingItem(Properties properties) {
        super(AetherSoundEvents.ITEM_ACCESSORY_EQUIP_ZANITE_RING, properties.repairable(AetherTags.Items.ZANITE_REPAIRING));
    }

}
