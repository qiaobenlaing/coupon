//
//  BMSQ_MemberCardEditViewController.h
//  BMSQS
//
//  Created by lxm on 15/7/29.
//  Copyright (c) 2015年 ;. All rights reserved.
//

#import "BaseViewController.h"
#import "TPKeyboardAvoidingTableView.h"

@interface BMSQ_MemberCardEditViewController : BaseViewController
{
    __weak IBOutlet TPKeyboardAvoidingTableView *_tableView;
    
    __strong UITextField *_contentTextFiled;
    __strong UITextField *_scoreTextField;
    __strong UITextField *_discountTextField;
    __strong UITextField *_eachYuanTextField;
    __strong UITextField *_eachYuanScoreTextFiled;
    __strong UITextField *_eachScoreTextField;
    __strong UITextField *_eachScoreYuanTextField;
    
    __strong UITextField *_expireMonthTextField;
    
    NSMutableDictionary *_inputDic;
}
@property(nonatomic,strong)NSArray *cardDataArray;
@property(nonatomic,strong)NSString *level;
@end
