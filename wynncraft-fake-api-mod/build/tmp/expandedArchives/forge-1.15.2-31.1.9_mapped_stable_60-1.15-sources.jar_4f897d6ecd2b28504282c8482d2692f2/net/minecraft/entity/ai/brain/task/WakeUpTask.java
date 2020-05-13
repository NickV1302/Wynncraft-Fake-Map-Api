package net.minecraft.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.world.server.ServerWorld;

public class WakeUpTask extends Task<LivingEntity> {
   public WakeUpTask() {
      super(ImmutableMap.of());
   }

   protected boolean func_212832_a_(ServerWorld worldIn, LivingEntity owner) {
      return !owner.getBrain().hasActivity(Activity.REST) && owner.isSleeping();
   }

   protected void func_212831_a_(ServerWorld worldIn, LivingEntity entityIn, long gameTimeIn) {
      entityIn.wakeUp();
   }
}