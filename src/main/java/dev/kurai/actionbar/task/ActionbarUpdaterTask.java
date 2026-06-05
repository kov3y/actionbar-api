package dev.kurai.actionbar.task;

import static net.kyori.adventure.text.Component.text;

import com.google.common.collect.Lists;
import dev.kurai.actionbar.entry.ActionbarEntry;
import dev.kurai.actionbar.service.ActionbarService;
import java.util.Comparator;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public final class ActionbarUpdaterTask implements Runnable {

  private final ActionbarService service;
  private final Function<Player, Audience> audienceProvider;

  @Override
  public void run() {
    for (final var player : Bukkit.getOnlinePlayers()) {
      final var actionbar = this.service.actionbar(player.getUniqueId());
      actionbar.entries().removeIf(ActionbarEntry::expired);

      final var entries = Lists.newArrayList(actionbar.entries());
      if (entries.isEmpty()) {
        continue;
      }

      final var component = text().append(text('»', NamedTextColor.DARK_GRAY)).appendSpace();
      entries.sort(Comparator.comparing(ActionbarEntry::key));

      for (final var entry : entries) {
        component.append(entry.value());

        if (!entry.key().equals(entries.getLast().key())) {
          component.appendSpace().append(text('❘', NamedTextColor.DARK_GRAY)).appendSpace();
        }
      }

      component.appendSpace().append(text('«', NamedTextColor.DARK_GRAY));
      this.audienceProvider.apply(player).sendActionBar(component);
    }
  }
}
