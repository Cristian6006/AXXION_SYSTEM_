<?php   

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;
use Carbon\Carbon;

class RefreshToken extends Model
{
    protected $fillable = [
        'user_id',
        'token_hash',
        'device_name',
        'ip_address',
        'user_agent',
        'expires_at',
        'last_used_at',
    ];

    protected $casts = [
        'expires_at' => 'datetime',
        'last_used_at' => 'datetime',
    ];

    public function user(): BelongsTo
    {
        return $this->belongsTo(Usuario::class);
    }

    public function isExpired(): bool
    {
        return $this->expires_at->isPast();
    }

    public function updateLastUsed(): void
    {
        $this->update(['last_used_at' => now()]);
    }

    public static function createForUser(Usuario $user, string $token, array $metadata = []): self
    {
        return self::create([
            'user_id' => $user->id,
            'token_hash' => hash('sha256', $token),
            'device_name' => $metadata['device_name'] ?? null,
            'ip_address' => $metadata['ip_address'] ?? null,
            'user_agent' => $metadata['user_agent'] ?? null,
            'expires_at' => Carbon::now()->addDays(30),
        ]);
    }

    public static function cleanupExpired(): int
    {
        return self::where('expires_at', '<', now())->delete();
    }
}