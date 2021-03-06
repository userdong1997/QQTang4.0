package com.wangrollin.qqtang.player;

import com.wangrollin.qqtang.constants.GameConstants;
import com.wangrollin.qqtang.element.Ball;
import com.wangrollin.qqtang.element.Item;
import com.wangrollin.qqtang.element.Maps;
import com.wangrollin.qqtang.element.MusicTool;

import java.util.Random;

public class AI extends Player {
    //protected int amount = MAX_BALL_CAPACITY, power = 5, speed = MAX_SPEED + 2;

    private Random random;

    public AI(Maps maps) {
        super(GameConstants.PLAYER_AI, maps);
        random = new Random();
    }

    // todo AI 主动去刺破 human
    // todo 让AI更像人, 躲开墙,吃道具,一点一点成长,攻击手段多样化
    // todo ai两个部分代码整理合并
    public void keepDoing() {
        pickupItem();
        countTimeToLoseIfPossible();
        beBombed();
    }

    public void setBall() {
        if (outlooking <= OUTLOOKING_CAN_SETBALL_UPPER_LIMIT) {
            maps.setBall(new Ball(this, getHeng(), getShu(), getShowBallPower(), getBallIcon(), maps));
            MusicTool.SET_BALL.play();
        }
    }

    //技能--斜泡    1继续执行 -1中断
    public int attackUpRight(int i) {
        if (i >= 83) return -1;
        if (i % 21 == 0) {
            if (isLineSafe(getHeng(), getShu()) && !isBorder(getHeng(), getShu())) setBall();
            else return -1;
        } else if (i % 21 >= 1 && i % 21 <= 10) {
            if (canGoRight()) goRight();
            else return -1;
        } else if (i % 21 >= 11 && i % 21 <= 20) {
            if (canGoUp()) goUp();
            else return -1;
        }
        return 1;
    }

    public int attackUpLeft(int i) {
        if (i >= 83) return -1;
        if (i % 21 == 0) {
            if (isLineSafe(getHeng(), getShu()) && !isBorder(getHeng(), getShu())) setBall();
            else return -1;
        } else if (i % 21 >= 1 && i % 21 <= 10) {
            if (canGoLeft()) goLeft();
            else return -1;
        } else if (i % 21 >= 11 && i % 21 <= 20) {
            if (canGoUp()) goUp();
            else return -1;
        }
        return 1;
    }

    public int attackDownLeft(int i) {
        if (i >= 83) return -1;
        if (i % 21 == 0) {
            if (isLineSafe(getHeng(), getShu()) && !isBorder(getHeng(), getShu())) setBall();
            else return -1;
        } else if (i % 21 >= 1 && i % 21 <= 10) {
            if (canGoLeft()) goLeft();
            else return -1;
        } else if (i % 21 >= 11 && i % 21 <= 20) {
            if (canGoDown()) goDown();
            else return -1;
        }
        return 1;
    }

    public int attackDownRight(int i) {
        if (i >= 83) return -1;
        if (i % 21 == 0) {
            if (isLineSafe(getHeng(), getShu()) && !isBorder(getHeng(), getShu())) setBall();
            else return -1;
        } else if (i % 21 >= 1 && i % 21 <= 10) {
            if (canGoRight()) goRight();
            else return -1;
        } else if (i % 21 >= 11 && i % 21 <= 20) {
            if (canGoDown()) goDown();
            else return -1;
        }
        return 1;
    }


    //    1继续执行 -1中断
    public int attackUp(int i) {
        if (i >= 55) return -1;
        else if (!canGoUp()) return -1;
        else if (getShu() - 1 < 0) return -1;
        else if (!isAttackStraightSafe(1)) return -1;
        if (i % 11 == 0) setBall();
        else goUp();
        return 1;
    }

    public int attackLeft(int i) {
        if (i >= 55) return -1;
        else if (!canGoLeft()) return -1;
        else if (getHeng() - 1 < 0) return -1;
        else if (!isAttackStraightSafe(2)) return -1;
        if (i % 11 == 0) setBall();
        else goLeft();
        return 1;
    }

    public int attackDown(int i) {
        if (i >= 55) return -1;
        else if (!canGoDown()) return -1;
        else if (getShu() + 1 > 7) return -1;
        else if (!isAttackStraightSafe(3)) return -1;
        if (i % 11 == 0) setBall();
        else goDown();
        return 1;
    }

    public int attackRight(int i) {
        if (i >= 55) return -1;
        else if (!canGoRight()) return -1;
        else if (getHeng() + 1 > 12) return -1;
        else if (!isAttackStraightSafe(4)) return -1;
        else if (i % 11 == 0) setBall();
        else goRight();
        return 1;
    }

    //长龙检查 oblique
    public boolean isAttackStraightSafe(int n) {

        if (n == 1) {
            int i = getHeng();
            int j = getShu() - 1;
            if (maps.isExplosion(i, j) || maps.isBall(i, j)) return false;
            for (int k = 0; k < 8; ++k) {
                if (isUpSafe(i, j - k - 1, k + 1) && isLeftSafe(i - k - 1, j, k + 1) && isRightSafe(i + k + 1, j, k + 1))
                    ;
                else return false;
            }
            return true;
        } else if (n == 2) {
            int i = getHeng() - 1;
            int j = getShu();
            if (maps.isExplosion(i, j) || maps.isBall(i, j)) return false;
            for (int k = 0; k < 8; ++k) {
                if (isUpSafe(i, j - k - 1, k + 1) && isLeftSafe(i - k - 1, j, k + 1) && isDownSafe(i, j + k + 1, k + 1))
                    ;
                else return false;
            }
            return true;
        } else if (n == 3) {
            int i = getHeng();
            int j = getShu() + 1;
            if (maps.isExplosion(i, j) || maps.isBall(i, j)) return false;
            for (int k = 0; k < 8; ++k) {
                if (isLeftSafe(i - k - 1, j, k + 1) && isDownSafe(i, j + k + 1, k + 1) && isRightSafe(i + k + 1, j, k + 1))
                    ;
                else return false;
            }
            return true;
        } else {
            int i = getHeng() + 1;
            int j = getShu();
            if (maps.isExplosion(i, j) || maps.isBall(i, j)) return false;
            for (int k = 0; k < 8; ++k) {
                if (isUpSafe(i, j - k - 1, k + 1) && isDownSafe(i, j + k + 1, k + 1) && isRightSafe(i + k + 1, j, k + 1))
                    ;
                else return false;
            }
            return true;
        }
    }

    //技能--V字弹花 此技能需要先检查战地在进行  进行时不会进行检查
    public int danhua(int i) {
        if (i == 0) setBall();
        else if (i >= 1 && i <= 10) goRight();
        else if (i >= 11 && i <= 30) goDown();
        else if (i == 31) setBall();
        else if (i >= 32 && i <= 41) goRight();
        else if (i >= 42 && i <= 51) goUp();
        else if (i == 52) setBall();

        if (i >= 0 && i <= 52) return 1;
        else return -1;
    }

    //技能释放前的安全检查
    public boolean danhuaSafe() {
        for (int i = getHeng(); i < getHeng() + 4; ++i)
            for (int j = getShu(); j < getShu() + 4; ++j)
                if (j <= 7 && j >= 0 && i <= 12 && i >= 0) {
                    if (maps.isBall(i, j) || maps.isExplosion(i, j)
                            || maps.isWall(i, j) || !isLineSafe(i, j))
                        return false;
                } else return false;

        return true;
    }

    public void scatterItemFox() {
        // TODO: 18/5/22 找到更好的解决方法
        if (outlooking <= OUTLOOKING_CAN_MOVE_UPPER_LIMIT) {
            for (int k = 0; k < 15; ++k) {
                int i = random.nextInt(13);
                int j = random.nextInt(8);
                if (i <= 12 && i >= 0 && j <= 7 && j >= 0 && !maps.isItem(i, j)
                        && !maps.isBall(i, j) && !maps.isExplosion(i, j)
                        && !maps.isWall(i, j))
                    maps.setItem(i, j, Item.createItemFox());
            }
            MusicTool.SCATTER_ITEM.play();
        }
    }

    //以自我为中心的方框内是否安全
    private boolean isBoxSafe() {
        for (int i = getHeng() - 1; i < getHeng() + 2; ++i)
            for (int j = getShu() - 1; j < getShu() + 2; ++j)
                if (j <= 7 && j >= 0 && i <= 12 && i >= 0)
                    if (isLineSafe(i, j) == false) return false;
        return true;
    }

    private boolean isBorder(int i, int j) {
        if (i <= 0 || j <= 0 || i >= 12 || j >= 7) return true;
        else return false;
    }

    //象限的名义查找玩家的位置
    public int getPlayerQuadrant() {
        if (getAnotherPlayer().getJudgeXPosition() > getJudgeXPosition()
                && getAnotherPlayer().getJudgeYPosition() > getJudgeYPosition()) {
            return 4;
        }
        if (getAnotherPlayer().getJudgeXPosition() < getJudgeXPosition()
                && getAnotherPlayer().getJudgeYPosition() > getJudgeYPosition()) {
            return 3;
        }
        if (getAnotherPlayer().getJudgeXPosition() > getJudgeXPosition()
                && getAnotherPlayer().getJudgeYPosition() < getJudgeYPosition()) {
            return 1;
        }
        if (getAnotherPlayer().getJudgeXPosition() < getJudgeXPosition()
                && getAnotherPlayer().getJudgeYPosition() < getJudgeYPosition()) {
            return 2;
        }
        return 0;
    }

    //-2必死无疑   0表示不用动  1表示向上走  2表示左走 3表示下走 4表示右走
    public int findway() {
        if (isLineSafe(getHeng(), getShu())) return 0;
        if (maps.isBall(getHeng(), getShu())) {
            if (gw(getHeng(), getShu()) && myfindway(getHeng(), getShu() - 1)) return 1;
            if (ga(getHeng(), getShu()) && myfindway(getHeng() - 1, getShu())) return 2;
            if (gs(getHeng(), getShu()) && myfindway(getHeng(), getShu() + 1)) return 3;
            if (gd(getHeng(), getShu()) && myfindway(getHeng() + 1, getShu())) return 4;
            return -2;
        }
        for (int i = 0; ; ++i) {
            if (!gw(getHeng(), getShu() - i)) break;
            if (isLineSafe(getHeng(), getShu() - i - 1)) return 1;
        }
        for (int i = 0; ; ++i) {
            if (!ga(getHeng() - i, getShu())) break;
            if (isLineSafe(getHeng() - i - 1, getShu())) return 2;
        }
        for (int i = 0; ; ++i) {
            if (!gs(getHeng(), getShu() + i)) break;
            if (isLineSafe(getHeng(), getShu() + i + 1)) return 3;
        }
        for (int i = 0; ; ++i) {
            if (!gd(getHeng() + i, getShu())) break;
            if (isLineSafe(getHeng() + i + 1, getShu())) return 4;
        }

        if (gw(getHeng(), getShu())) return 1;
        else if (ga(getHeng(), getShu())) return 2;
        else if (gs(getHeng(), getShu())) return 3;
        else if (gd(getHeng(), getShu())) return 4;
        return -2;
    }

    //0表示不动
    public int getaway() {
        if (getAnotherPlayer().getHeng() - getHeng() > 0 && ga(getHeng(), getShu())
                && isLineSafe(getHeng() - 1, getShu()))
            return 2;
        if (getAnotherPlayer().getHeng() - getHeng() < 0 && gd(getHeng(), getShu())
                && isLineSafe(getHeng() + 1, getShu()))
            return 4;
        if (getAnotherPlayer().getShu() - getShu() > 0 && gw(getHeng(), getShu())
                && isLineSafe(getHeng(), getShu() - 1))
            return 1;
        if (getAnotherPlayer().getShu() - getShu() < 0 && gs(getHeng(), getShu())
                && isLineSafe(getHeng(), getShu() + 1))
            return 3;

        if (ga(getHeng(), getShu()) && isLineSafe(getHeng() - 1, getShu()))
            return 2;
        if (gd(getHeng(), getShu()) && isLineSafe(getHeng() + 1, getShu()))
            return 4;
        if (gw(getHeng(), getShu()) && isLineSafe(getHeng(), getShu() - 1))
            return 1;
        if (gs(getHeng(), getShu()) && isLineSafe(getHeng(), getShu() + 1))
            return 3;

        return 0;
    }

    public boolean isNearWithHumanPlayer() {
        if (Math.abs(getAnotherPlayer().getHeng() - getHeng()) <= 2
                && Math.abs(getAnotherPlayer().getShu() - getShu()) <= 2)
            return true;
        else return false;
    }


    //-1表示现在应该跑  0现在安全了去追击玩家吧   1现在还不太安全  别乱动 再看看应该干什么
    public int safecheck() {
        if (!isLineSafe(getHeng(), getShu())) return -1;
        else if (isBoxSafe()) return 0;
        else return 1;
    }


    //帮助函数///////////////////////////////////////////////////////////////////////////////////////////
    //让虎克行走
    public void goDown() {
        isUpPressed = false;
        isDownPressed = true;
        isLeftPressed = false;
        isRightPressed = false;
        move();
    }

    public void goUp() {
        isUpPressed = true;
        isDownPressed = false;
        isLeftPressed = false;
        isRightPressed = false;
        move();
    }

    public void goLeft() {
        isUpPressed = false;
        isDownPressed = false;
        isLeftPressed = true;
        isRightPressed = false;
        move();
    }

    public void goRight() {
        isUpPressed = false;
        isDownPressed = false;
        isLeftPressed = false;
        isRightPressed = true;
        move();
    }

    private void stopGoing() {
        isUpPressed = false;
        isDownPressed = false;
        isLeftPressed = false;
        isRightPressed = false;
    }

    //判断此位置是否安全  0表示位置  1表示安全 2表示死亡   判断了 边界，墙壁，炸弹射程
    private boolean isUpSafe(int i, int j, int l) {
        if (j < 0) return true;
        else if (maps.isWall(i, j)) return true;
            //else if(com.wangrollin.qqtang.element.Maps.isExp(i,j)==true) return false;
        else if (!maps.isBall(i, j)) return true;
        else if (maps.isBall(i, j) && maps.getBall(i, j).getPower() < l) return true;
        else return false;

    }

    private boolean isLeftSafe(int i, int j, int l) {
        if (i < 0) return true;
        else if (maps.isWall(i, j)) return true;
            //else if(com.wangrollin.qqtang.element.Maps.isExp(i,j)==true) return false;
        else if (!maps.isBall(i, j)) return true;
        else if (maps.isBall(i, j) && maps.getBall(i, j).getPower() < l) return true;
        else return false;
    }

    private boolean isDownSafe(int i, int j, int l) {
        if (j > 7) return true;
        else if (maps.isWall(i, j)) return true;
            //else if(com.wangrollin.qqtang.element.Maps.isExp(i,j)==true) return false;
        else if (!maps.isBall(i, j)) return true;
        else if (maps.isBall(i, j) && maps.getBall(i, j).getPower() < l) return true;
        else return false;
    }

    private boolean isRightSafe(int i, int j, int l) {
        if (i > 12) return true;
        else if (maps.isWall(i, j)) return true;
            //else if(com.wangrollin.qqtang.element.Maps.isExp(i,j)==true) return false;
        else if (!maps.isBall(i, j)) return true;
        else if (maps.isBall(i, j) && maps.getBall(i, j).getPower() < l) return true;
        else return false;
    }

    //判断如果人在 i，j 点是否横竖安全
    private boolean isLineSafe(int xPosition, int yPosition) {
        if (maps.isExplosion(xPosition, yPosition) || maps.isBall(xPosition, yPosition)) {
            return false;
        }
        for (int k = 0; k < 8; ++k) {
            if (!(isUpSafe(xPosition, yPosition - k - 1, k + 1)
                    && isLeftSafe(xPosition - k - 1, yPosition, k + 1)
                    && isDownSafe(xPosition, yPosition + k + 1, k + 1)
                    && isRightSafe(xPosition + k + 1, yPosition, k + 1))) {
                return false;
            }
        }
        return true;
    }


    //能否移动到另一个格子
    private boolean gw(int i, int j) {
        boolean wall = false;
        boolean ball = false;
        boolean bianjie = false;
        if (j > 0)
            bianjie = true;
        if ((j - 1 >= 0 && !maps.isWall(i, j - 1))
                || (j - 1 >= 0 && maps.isWall(i, j - 1) && getJudgeYPosition() > ((j - 1) * 50 + 65))
                || (j - 1 < 0 && !maps.isWall(i, j)))
            wall = true;
        if ((j - 1 >= 0 && !maps.isBall(i, j - 1))
                || (j - 1 >= 0 && maps.isBall(i, j - 1) && getJudgeYPosition() > ((j - 1) * 50 + 65))
                || (j - 1 < 0 && !maps.isBall(i, j)))
            ball = true;

        return (wall && ball && bianjie);

    }

    private boolean ga(int i, int j) {

        boolean wall = false;
        boolean ball = false;
        boolean bianjie = false;
        if (i > 0)
            bianjie = true;
        if ((i - 1 >= 0 && !maps.isBall(i - 1, j))
                || (i - 1 >= 0 && maps.isBall(i - 1, j) && getJudgeXPosition() > (i - 1) * 50 + 65)
                || (i - 1 < 0 && !maps.isBall(i, j)))
            ball = true;
        if ((i - 1 >= 0 && !maps.isWall(i - 1, j))
                || (i - 1 >= 0 && maps.isWall(i - 1, j) && getJudgeXPosition() > (i - 1) * 50 + 65)
                || (i - 1 < 0 && !maps.isWall(i, j)))
            wall = true;
        return (wall && ball && bianjie);

    }


    public boolean gs(int i, int j) {

        boolean wall = false;
        boolean ball = false;
        boolean bianjie = false;
        if (j < 7)
            bianjie = true;
        if ((j + 1 <= 7 && !maps.isBall(i, j + 1))
                || (j + 1 <= 7 && maps.isBall(i, j + 1) && getJudgeYPosition() < ((j + 1) * 50 - 15))
                || (j + 1 > 7 && !maps.isBall(i, j)))
            ball = true;
        if ((j + 1 <= 7 && !maps.isWall(i, j + 1))
                || (j + 1 <= 7 && maps.isWall(i, j + 1) && getJudgeYPosition() < ((j + 1) * 50 - 15))
                || (j + 1 > 7 && !maps.isWall(i, j)))
            wall = true;
        return (wall && ball && bianjie);
    }

    public boolean gd(int i, int j) {

        boolean wall = false;
        boolean ball = false;
        boolean bianjie = false;
        if (i < 12)
            bianjie = true;
        if ((i + 1 <= 12 && !maps.isBall(i + 1, j))
                || (i + 1 <= 12 && maps.isBall(i + 1, j) && getJudgeXPosition() < (i + 1) * 50 - 15)
                || (i + 1 > 12 && !maps.isBall(i, j)))
            ball = true;
        if ((i + 1 <= 12 && !maps.isWall(i + 1, j))
                || (i + 1 <= 12 && maps.isWall(i + 1, j) && getJudgeXPosition() < (i + 1) * 50 - 15)
                || (i + 1 > 12 && !maps.isWall(i, j)))
            wall = true;
        return (wall && ball && bianjie);
    }

    public boolean stillin(int i, int j) {
        if (i <= 12 && i >= 0 && j <= 7 && j >= 0) return true;
        else return false;
    }

    /*public void move() {
        if (isRightPressed && outlooking <= OUTLOOKING_CAN_MOVE_UPPER_LIMIT) {
            currentPlayerIcon = orginGoRightIcon;
            if (canGoRight()) setJudgeXPosition(getJudgeXPosition() + getRealSpeed());
            stopGoing();
        } else if (isLeftPressed) {
            currentPlayerIcon = originGoLeftIcon;
            if (canGoLeft()) setJudgeXPosition(getJudgeXPosition() - getRealSpeed());
            stopGoing();
        } else if (isUpPressed) {
            currentPlayerIcon = originGoUpIcon;
            if (canGoUp()) setJudgeYPosition(getJudgeYPosition() - getRealSpeed());
            stopGoing();
        } else if (isDownPressed) {
            currentPlayerIcon = originGoDownIcon;
            if (canGoDown()) setJudgeYPosition(getJudgeYPosition() + getRealSpeed());
            stopGoing();
        }

    }*/

    public void pickupItem() {
        if (maps.isItem(getHeng(), getShu())) {
            MusicTool.PICKUP_ITEM.play();
            maps.removeItem(getHeng(), getShu());
            MusicTool.PICKUP_ITEM.play();
        }
    }

    /*public boolean isBeBombed() {
        return maps.isExplosion(getHeng(), getShu());
    }*/

    public boolean myfindway(int i, int j) {
        if (isLineSafe(i, j)) return true;

        for (int k = 0; ; ++k) {
            if (!gw(i, j - k)) break;
            if (isLineSafe(i, j - k - 1)) return true;
        }
        for (int k = 0; ; ++k) {
            if (!ga(i - k, j)) break;
            if (isLineSafe(i - k - 1, j)) return true;
        }
        for (int k = 0; ; ++k) {
            if (!gs(i, j + k)) break;
            if (isLineSafe(i, j + k + 1)) return true;
        }
        for (int k = 0; ; ++k) {
            if (!gd(i + k, j)) break;
            if (isLineSafe(i + k + 1, j)) return true;
        }
        return false;
    }

}