package dev.kurai.actionbar;

import dev.kurai.actionbar.entry.ActionbarEntry;
import java.time.Duration;
import java.util.Collection;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public interface Actionbar {

  @Contract(value = " -> new", pure = true)
  static @NonNull Actionbar create() {
    return new ActionbarImpl();
  }

  Collection<ActionbarEntry> entries();

  default void registerEntry(final Key key, final Component value) {
    this.registerEntry(new ActionbarEntry(key, value));
  }

  default void registerEntry(final Key key, final Component value, final Duration duration) {
    this.registerEntry(new ActionbarEntry(key, value, duration));
  }

  void registerEntry(final ActionbarEntry entry);

  void unregisterEntry(final Key key);

  ActionbarEntry entry(final Key key);
}
