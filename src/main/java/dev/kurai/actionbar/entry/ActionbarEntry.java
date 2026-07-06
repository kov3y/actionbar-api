package dev.kurai.actionbar.entry;

import java.time.Duration;
import java.time.Instant;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.Contract;

/**
 * An immutable entry in an action bar, consisting of an identifying {@link Key}, a display {@link
 * Component}, and a lifetime {@link Duration}.
 *
 * <p>Entries created without a duration or with {@link Duration#ZERO} never expire. Entries created
 * with a positive duration expire once their lifetime has elapsed, as determined by {@link
 * #expired()}.
 */
public sealed interface ActionbarEntry extends Keyed, ComponentLike permits ActionbarEntryImpl {

  /**
   * Creates an entry with the specified lifetime.
   *
   * @param key the identifier for this entry
   * @param componentLike the componentLike to display
   * @param duration the lifetime of this entry; use {@link Duration#ZERO} for no expiry
   * @return a new action-bar entry
   */
  @Contract(value = "_, _, _ -> new", pure = true)
  static ActionbarEntry actionbarEntry(
      final Key key, final ComponentLike componentLike, final Duration duration) {
    return new ActionbarEntryImpl(key, componentLike, duration);
  }

  /**
   * Creates a persistent entry that never expires.
   *
   * @param key the identifier for this entry
   * @param componentLike the componentLike to display
   * @return a new persistent action-bar entry
   */
  @Contract(value = "_, _ -> new", pure = true)
  static ActionbarEntry actionbarEntry(final Key key, final ComponentLike componentLike) {
    return actionbarEntry(key, componentLike, Duration.ZERO);
  }

  /**
   * Returns the lifetime of this entry.
   *
   * @return the lifetime, where {@link Duration#ZERO} indicates no expiry
   */
  Duration duration();

  /**
   * Returns the instant at which this entry was created.
   *
   * @return the creation instant
   */
  Instant creationTime();

  /**
   * Returns whether this entry's lifetime has elapsed.
   *
   * @return {@code true} if expired, otherwise {@code false}
   */
  boolean expired();
}
