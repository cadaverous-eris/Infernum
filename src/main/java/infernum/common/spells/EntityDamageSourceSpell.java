package infernum.common.spells;

import net.minecraft.entity.Entity;
import net.minecraft.util.EntityDamageSource;

public class EntityDamageSourceSpell extends EntityDamageSource {

	public EntityDamageSourceSpell(Entity damageSourceEntityIn) {
		super("magic", damageSourceEntityIn);
		setDamageBypassesArmor();
		setMagicDamage();
	}

}
