package dev.kurai.actionbar;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;

import dev.kurai.actionbar.entry.ActionbarEntry;
import net.kyori.adventure.key.Key;

final class ActionbarImpl implements Actionbar {

  private final Map<Key, ActionbarEntry> entries;

  public ActionbarImpl() {
    this.entries = Maps.newHashMap();
  }

  @Override
  public Collection<ActionbarEntry> entries() {
    return this.entries.values();
  }

  @Override
  public void registerEntry(final ActionbarEntry entry) {
    this.entries.put(entry.key(), entry);
  }

  @Override
  public void unregisterEntry(final Key key) {
    this.entries.remove(key);
  }

  @Override
  public ActionbarEntry entry(final Key key) {
    return this.entries.get(key);
  }
}
