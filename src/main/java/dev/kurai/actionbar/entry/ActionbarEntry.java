package dev.kurai.actionbar.entry;

import java.time.Duration;
import java.time.Instant;
import lombok.Getter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.Component;

@Getter
public final class ActionbarEntry implements Keyed {

  private final Key key;
  private final Component value;

  private final Instant creationTime;
  private final Duration duration;

  public ActionbarEntry(final Key key, final Component value) {
    this.key = key;
    this.value = value;

    this.creationTime = Instant.now();

    this.duration = Duration.ZERO;
  }

  public ActionbarEntry(final Key key, final Component value, final Duration duration) {
    this.key = key;
    this.value = value;

    this.creationTime = Instant.now();

    this.duration = duration;
  }

  public boolean expired() {
    return !this.duration.isZero() && Instant.now().isAfter(this.creationTime.plus(this.duration));
  }
}
