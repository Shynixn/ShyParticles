# Permissions Guide

This guide explains all permission nodes available in ShyParticles and how to properly configure them for your server. Understanding permissions is crucial for controlling who can manage particle effects and which effects players can access.

## 🔐 Permission Levels

ShyParticles uses two permission levels:

* **👤 User Level**: Permissions that regular players can have
* **🛡️ Admin Level**: Permissions that should only be given to trusted staff

---

## 📋 Complete Permission Reference

| Permission | Level | Description | Required For |
|------------|-------|-------------|--------------|
| `shyparticles.command` | 🛡️ Admin | Use the base `/shyparticles` command | Running any command |
| `shyparticles.reload` | 🛡️ Admin | Reload particle effect configurations | `/shyparticles reload` |
| `shyparticles.list` | 🛡️ Admin | List available particle effects | `/shyparticles list` |
| `shyparticles.play` | 🛡️ Admin | Play particle effects at any location | `/shyparticles play` |
| `shyparticles.stop` | 🛡️ Admin | Stop particle effects at any location | `/shyparticles stop` |
| `shyparticles.follow` | 👤 User | Play follow particle effects on yourself | `/shyparticles follow` |
| `shyparticles.followother` | 🛡️ Admin | Play follow particle effects on other players | `/shyparticles follow <player>` |
| `shyparticles.stopfollow` | 👤 User | Stop follow particle effects on yourself | `/shyparticles stopfollow` |
| `shyparticles.stopfollowother` | 🛡️ Admin | Stop follow particle effects on other players | `/shyparticles stopfollow <player>` |
| `shyparticles.effect.start.*` | 👤 User | Access to **all** particle effects | Starting any particle effect |
| `shyparticles.effect.start.<name>` | 👤 User | Access to a **specific** particle effect | Starting named particle effect |
| `shyparticles.effect.visible.*` | 👤 User | View **all** particle effects | Seeing any particle effect |
| `shyparticles.effect.visible.<name>` | 👤 User | View a **specific** particle effect | Seeing named particle effect |

