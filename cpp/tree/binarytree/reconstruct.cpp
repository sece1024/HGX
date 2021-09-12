//
// Created by sece on 2021/9/12.
// 将reconstruct.cpp作为可编译文件的方法：
//https://blog.csdn.net/weixin_43848437/article/details/107397445?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_title~default-1.no_search_link&spm=1001.2101.3001.4242
//

#include "reconstruct.h"
#include <iostream>
int main(){
//    std::cout<<"hello, world"<<std::endl;
    return 0;
}

/*
 * 问题：输入二叉树的前序和中序遍历序列，得到完整二叉树（不含重复数字）;
 * 思路：根据前序序列得到根节点的值，根据这个值在中序序列招到根节点所在位置，该位置左右两侧的节点分别是二叉树的左子树和右子树;
 * 由于先序遍历的序列顺序为{根节点，左1，左2...左i，右1，右2，...，右j}这种形式，所以只要知道了左子树和右子树的节点数目就能得到左右子树的先序遍历序列;
 * 
 * */