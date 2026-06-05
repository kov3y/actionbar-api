package dev.kurai.actionbar.service;

import java.util.UUID;
import java.util.function.Function;

import dev.kurai.actionbar.Actionbar;
import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public interface ActionbarService {

  @Contract(value = "_, _ -> new", pure = true)
  static @NonNull ActionbarService create(
      final Plugin plugin, final Function<Player, Audience> audienceProvider) {
    return new ActionbarServiceImpl(plugin, audienceProvider);
  }

  Actionbar actionbar(final UUID holder);
}
