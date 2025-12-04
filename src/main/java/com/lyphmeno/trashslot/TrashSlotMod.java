package com.lyphmeno.trashslot;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrashSlotMod implements ModInitializer {

    // Mod id used everywhere (logs, resources, etc.)
    public static final String MOD_ID = "trashslot";

    // Simple logger to print messages in the console
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("TrashSlot mod initializing...");

        // Register the /trashhand command
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                    CommandManager.literal("trashhand")
                            .executes(context -> {
                                // Ensure the command source is a player
                                if (!(context.getSource().getEntity() instanceof ServerPlayerEntity player)) {
                                    context.getSource().sendError(Text.literal("This command can only be used by a player."));
                                    return 0;
                                }

                                // Get item in main hand
                                ItemStack stack = player.getMainHandStack();

                                if (stack.isEmpty()) {
                                    // Nothing to delete
                                    context.getSource().sendFeedback(
                                            () -> Text.literal("Your main hand is already empty."),
                                            false
                                    );
                                } else {
                                    // Delete the item by replacing it with an empty stack
                                    player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);

                                    context.getSource().sendFeedback(
                                            () -> Text.literal("Item deleted."),
                                            false
                                    );
                                }

                                return 1;
                            })
            );
        });

        LOGGER.info("TrashSlot command /trashhand registered.");
    }
}
