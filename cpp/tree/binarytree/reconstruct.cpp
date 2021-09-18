//
// Created by sece on 2021/9/12.
// 将reconstruct.cpp作为可编译文件的方法：
//https://blog.csdn.net/weixin_43848437/article/details/107397445?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_title~default-1.no_search_link&spm=1001.2101.3001.4242
//

/*
 * 问题：输入二叉树的前序和中序遍历序列，得到完整二叉树（不含重复数字）;
 * 思路：根据前序序列得到根节点的值，根据这个值在中序序列招到根节点所在位置，该位置左右两侧的节点分别是二叉树的左子树和右子树;
 * 由于先序遍历的序列顺序为{根节点，左1，左2...左i，右1，右2，...，右j}这种形式，所以只要知道了左子树和右子树的节点数目就能得到左右子树的先序遍历序列;
 *
 * 样例：Preorder = {1, 2, 4, 7, 3, 5, 6, 8}
 * 	 Inorder  = {4, 7, 2, 1, 5, 3, 8, 6}
 * */
#include "reconstruct.h"
#include <iostream>
using namespace std;
struct BinaryTreeNode{
	int m_nValue;
	BinaryTreeNode* m_pLeft;
	BinaryTreeNode* m_pRight;

};

void printNode(BinaryTreeNode* root){
	if (root != nullptr) {
		cout<<root->m_nValue<<endl;
	}
	if (root->m_pLeft != nullptr) {
		printNode(root->m_pLeft);
	}
	if (root->m_pRight != nullptr) {
		printNode(root->m_pRight);
	}
}
BinaryTreeNode* ConstructCore(
	int* startPreorder, int* endPreorder,
	int* startInorder, int* endInorder
){
	// 前序遍历序列的第一个数字是根节点
	int rootValue = startPreorder[0];
	BinaryTreeNode* root = new BinaryTreeNode();
	root->m_nValue = rootValue;
	root->m_pLeft = root->m_pRight = nullptr;

	if (startPreorder == endPreorder) {
		if (startInorder == endInorder
		&& *startPreorder == *startInorder) {
			return root;
		}else{
			cout<<("Invalid input.")<<endl;
		}
	}
	// 在中序遍历中找到根节点的指
	int *rootInorder = startInorder;
	while(rootInorder <= endInorder && *rootInorder != rootValue){
		++rootInorder;
	}
	if (rootInorder == endInorder && *rootInorder != rootValue) {
		cout<<("Invalid input.")<<endl;
	}

	int leftLength = rootInorder - startInorder;
	int* leftPreorderEnd = startPreorder + leftLength;
	if (leftLength > 0) {
		// 构建左子数
		root->m_pLeft = ConstructCore(startPreorder + 1,
		leftPreorderEnd, startInorder, rootInorder - 1);
	}
	if (leftLength < endPreorder - startPreorder) {
		// 构建右子树
		root->m_pRight = ConstructCore(leftPreorderEnd + 1,
		endPreorder, rootInorder + 1, endInorder);
	}
	return root;
}

BinaryTreeNode* Construct(int* preorder, int* inorder, int length){
	if(preorder == nullptr || inorder == nullptr || length <= 0){
		return nullptr;
	}
	return ConstructCore(preorder, preorder + length - 1,
	inorder, inorder + length - 1);

}


int main(){
    //std::cout<<"hello, world"<<std::endl;
    int Preorder[8] = {1, 2, 4, 7, 3, 5, 6, 8};
    int Inorder[8]  = {4, 7, 2, 1, 5, 3, 8, 6};
		BinaryTreeNode* root = Construct(Preorder, Inorder, 8);
		printNode(root);
    return 0;
}
