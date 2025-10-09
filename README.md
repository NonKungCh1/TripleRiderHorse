# 🐴 TripleRiderHorse

[![Maven Build Status](https://github.com/NonKungCh1/TripleRiderHorse/actions/workflows/maven.yml/badge.svg)](https://github.com/NonKungCh1/TripleRiderHorse/actions/workflows/maven.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

**TripleRiderHorse** เป็นปลั๊กอินสำหรับเซิร์ฟเวอร์ $\text{Paper/Spigot}$ ที่ช่วยให้ผู้เล่นหลายคนสามารถขี่ม้าตัวเดียวกันได้พร้อมกัน โดยการซ้อนที่นั่งผู้เล่นที่ด้านหลังม้าอย่างเป็นระเบียบ และสามารถปรับจำนวนผู้โดยสารสูงสุดได้ทั้งในไฟล์คอนฟิกและในเกม

## ✨ คุณสมบัติหลัก

* **Multi-Seating:** อนุญาตให้ผู้เล่นหลายคนคลิกขวาที่ม้าตัวเดียวเพื่อโดยสารต่อกันเป็นแถว
* **In-Game Configuration:** สามารถปรับจำนวนผู้โดยสารสูงสุดได้ทันทีในเกมด้วยคำสั่ง
* **Dynamic Seating:** ใช้ $\text{Armor Stands}$ ล่องหนเป็นที่นั่งชั่วคราวและจะถูกกำจัดออกทันทีเมื่อผู้เล่นลงจากม้า
* **Configurable Max Riders:** ควบคุมจำนวนผู้เล่นสูงสุดที่อนุญาตให้ขี่ม้าได้ (รวมคนขับด้วย)

---

## 🚀 การติดตั้งและการใช้งาน

### 1. การติดตั้ง

1.  ดาวน์โหลดไฟล์ `.jar` ล่าสุดจากหน้า [Releases](https://github.com/NonKungCh1/TripleRiderHorse/releases) หรือจากการ $\text{Build}$ ผ่าน $\text{GitHub Actions}$.
2.  วางไฟล์ `TripleRiderHorse-1.0-SNAPSHOT.jar` ลงในโฟลเดอร์ `plugins/` ของเซิร์ฟเวอร์คุณ
3.  รีสตาร์ทหรือโหลดปลั๊กอินใหม่ (`/reload` หรือ `/plugman reload TripleRiderHorse`).

### 2. ไฟล์ Config (`config.yml`)

ไฟล์คอนฟิกจะถูกสร้างขึ้นในโฟลเดอร์ `plugins/TripleRiderHorse/` โดยมีค่าเริ่มต้นดังนี้:

```yaml
# กำหนดจำนวนผู้เล่นสูงสุดที่สามารถนั่งม้าตัวเดียวได้
# ค่านี้รวมถึงคนขับด้วย: 
# 3 = คนขับ 1 + ผู้โดยสาร 2
max-riders: 3

```
## ⌨️ คำสั่งและ Permissions
* **ปลั๊กอินใช้คำสั่งหลัก /triplerider (หรือ /tr) เพื่อจัดการการตั้งค่าต่างๆ

|    คำสั่ง        |    	คำอธิบาย	       |        Permission   |

|/triplerider |	แสดงหน้าช่วยเหลือและจำนวนผู้โดยสารสูงสุดปัจจุบัน	None |

|/triplerider reload	| รีโหลด ไฟล์ config.yml และอัปเดตค่าในปลั๊กอิน	triplerider.admin |

|/triplerider setmax <number> |	 ตั้งค่าจำนวนผู้โดยสารสูงสุดใหม่ ทันทีในเกม และบันทึกค่าลงใน config.yml	triplerider.admin |
