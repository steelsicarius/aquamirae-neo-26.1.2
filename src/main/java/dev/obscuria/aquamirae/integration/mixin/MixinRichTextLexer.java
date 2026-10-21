package dev.obscuria.aquamirae.integration.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;

/**
 * Fragmentum 4.0.5/4.1.0 (MC 26.1): _RichTextLexer keeps the live Matcher in
 * every span and reads its groups from emitTag/emitTemplate long after the
 * find() loops exhausted it. On Java 25, Matcher#toMatchResult returns a
 * non-Matcher snapshot, so the span code falls back to the already-exhausted
 * matcher and every call to group() throws "No match found" — crashing any
 * tooltip or lore that contains rich-text tags (e.g. [color=white]).
 *
 * The patch redirects toMatchResult() to return a fresh matcher frozen on the
 * current match, so each span reads its own groups regardless of matcher state.
 * require = 0 keeps this harmless once Fragmentum ships a fixed lexer.
 */
@Mixin(targets = "dev.obscuria.fragmentum.v2.core.common.richtext._RichTextLexer")
public abstract class MixinRichTextLexer {

    @Redirect(require = 0,
            method = "tokenize(Ljava/lang/String;Ljava/lang/Object;)Ljava/util/List;",
            at = @At(value = "INVOKE", target = "Ljava/util/regex/Matcher;toMatchResult()Ljava/util/regex/MatchResult;"))
    private static MatchResult aquamirae$freezeSpanMatcher(Matcher instance) {
        Matcher frozen = instance.pattern().matcher(instance.group());
        frozen.find();
        return frozen;
    }
}
