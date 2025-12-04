package com.lyphmeno.trashslot;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;

import net.minecraft.world.item.ItemStack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class TrashSlotMod implements ModInitializer {

    public static final String MOD_ID = "trashslot";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("TrashSlot mod initializing...");

        // Register the /trashhand command
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                    Commands.literal("trashhand")
                            .executes(context -> {
                                // Ensure the command source is a player
                                var entity = context.getSource().getEntity();
                                if (!(entity instanceof ServerPlayer player)) {
                                    context.getSource().sendFailure(
                                            Component.literal("This command can only be used by a player.")
                                    );
                                    return 0;
                                }

                                // Get item in main hand
                                ItemStack stack = player.getMainHandItem();

                                if (stack.isEmpty()) {
                                    context.getSource().sendSuccess(
                                            () -> Component.literal("Your main hand is already empty."),
                                            false
                                    );
                                    return 0;
                                }

                                // Delete the item by replacing it with an empty stack
                                player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);

                                context.getSource().sendSuccess(
                                        () -> Component.literal("Item deleted."),
                                        false
                                );

                                return 1;
                            })
            );
        });

        LOGGER.info("TrashSlot /trashhand command registered.");
    }
}
