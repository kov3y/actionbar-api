package dev.kurai.actionbar.task;

import com.google.common.collect.Lists;
import dev.kurai.actionbar.Actionbar;
import dev.kurai.actionbar.ActionbarService;
import dev.kurai.actionbar.entry.ActionbarEntry;
import dev.kurai.actionbar.style.ActionbarStyle;
import java.util.Comparator;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Runnable task that runs every tick to refresh every online player's action bar.
 *
 * <p>On each invocation it:
 *
 * <ol>
 *   <li>Evicts expired {@link dev.kurai.actionbar.entry.ActionbarEntry entries} from each player's
 *       {@link Actionbar}.
 *   <li>Sorts remaining entries by their {@link net.kyori.adventure.key.Key}.
 *   <li>Composes a single {@link net.kyori.adventure.text.Component} using the service's {@link
 *       ActionbarStyle} (prefix, separators, suffix) and sends it via the configured audience
 *       provider.
 * </ol>
 *
 * <p>Players with no entries are skipped — no packet is sent.
 */
@RequiredArgsConstructor
public final class ActionbarUpdaterTask implements Runnable {

  private final ActionbarService actionbarService;
  private final Function<Player, Audience> audienceProvider;

  @Override
  public void run() {
    final ActionbarStyle actionbarStyle = this.actionbarService.style();
    for (final Player player : Bukkit.getOnlinePlayers()) {
      final Actionbar actionbar = this.actionbarService.actionbar(player.getUniqueId());
      actionbar.unregisterEntriesIf(ActionbarEntry::expired);

      final var actionbarEntries = Lists.newArrayList(actionbar.entries());
      if (actionbarEntries.isEmpty()) {
        continue;
      }

      final TextComponent.Builder component =
          Component.text().append(actionbarStyle.prefix()).appendSpace();
      actionbarEntries.sort(Comparator.comparing(ActionbarEntry::key));

      for (final ActionbarEntry actionbarEntry : actionbarEntries) {
        component.append(actionbarEntry.value());

        if (!actionbarEntry.key().equals(actionbarEntries.getLast().key())) {
          component.appendSpace().append(actionbarStyle.separator()).appendSpace();
        }
      }

      this.audienceProvider
          .apply(player)
          .sendActionBar(component.appendSpace().append(actionbarStyle.suffix()));
    }
  }
}
