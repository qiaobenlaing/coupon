//
//  ChargeViewController.m
//  BMSQS
//
//  Created by gh on 15/12/29.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "ChargeViewController.h"
#import "ChargeCell.h"
#define ChargeCellHeight 90

@interface ChargeViewController () <UITableViewDataSource, UITableViewDelegate, UITextFieldDelegate>


@property (nonatomic, strong)UITableView *tableView;
@property (nonatomic, assign)int textTag;

@end


@implementation ChargeViewController

- (id)init {
    self = [super init];
    if (self) {
        
        
    }
    
    return self;
    
}


- (void)viewDidLoad {
    
    [super viewDidLoad];
    
    [self setNavTitle:@"活动收费"];
    [self setNavBackItem];
    [self setViewUp];
    
    [self setRightBtn];
    [self setUpBottomView];
    
}

- (void)setViewUp {
    
    
    //Add keyboard notification
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onKeyboardNotification:) name:UIKeyboardWillHideNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onKeyboardNotification:) name:UIKeyboardWillShowNotification object:nil];
    
    
    self.view.backgroundColor = UICOLOR(239, 239, 244, 1);
    self.tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y-50)];
    self.tableView.dataSource = self;
    self.tableView.delegate = self;
    self.tableView.backgroundColor = UICOLOR(239, 239, 244, 1);
    self.tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.tableView.keyboardDismissMode = UIScrollViewKeyboardDismissModeInteractive;
    [self.view  addSubview:self.tableView];
    
    if (self.chargeData.count == 0) {
        [self addOneCell];
    }
    
}

- (void)setUpBottomView {
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    button.frame = CGRectMake(20, self.tableView.frame.size.height + self.tableView.frame.origin.y, APP_VIEW_WIDTH-40, 40);
    button.backgroundColor = APP_NAVCOLOR;
    [button addTarget:self action:@selector(addOneCell) forControlEvents:UIControlEventTouchUpInside];
    [button setTitle:@"添加规格" forState:UIControlStateNormal];
    [self.view addSubview:button];
    
}


- (void)addOneCell {
    NSMutableDictionary * dic = [NSMutableDictionary dictionaryWithCapacity:0];
    
    [dic setObject:@"" forKey:@"price"];
    [dic setObject:@"" forKey:@"des"];
    [dic setObject:@"" forKey:@"id"];
    [self.chargeData addObject:dic];
    
    [self.tableView reloadData];
}



- (void)leftFieldChanged:(UITextField *)sender
{
    
    self.textTag = (int)sender.tag;
    
    UITableViewCell *cell = (UITableViewCell *)[[sender superview] superview];
    NSIndexPath * indexPath = [self.tableView indexPathForCell:cell];
    NSMutableDictionary * mdic = [NSMutableDictionary dictionaryWithDictionary:self.chargeData[indexPath.row]];
    [self.chargeData removeObjectAtIndex:indexPath.row];
    [mdic setObject:sender.text forKey:@"des"];
    [self.chargeData insertObject:mdic atIndex:indexPath.row];
    
}
- (void)rightFieldChanged:(UITextField *)sender
{
    self.textTag = (int)sender.tag;
    
    UITableViewCell *cell = (UITableViewCell *)[[sender superview] superview];
    NSIndexPath * indexPath = [self.tableView indexPathForCell:cell];
    NSMutableDictionary * mdic = [NSMutableDictionary dictionaryWithDictionary:self.chargeData[indexPath.row]];
    [self.chargeData removeObjectAtIndex:indexPath.row];
    [mdic setObject:sender.text forKey:@"price"];
    [self.chargeData insertObject:mdic atIndex:indexPath.row];
    
}

#pragma mark - UITableView delegate
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    return  self.chargeData.count;
    
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *identifier = @"ChargeCell";
    ChargeCell *cell = (ChargeCell *)[tableView dequeueReusableCellWithIdentifier:identifier];
    if (!cell) {
        cell = [[ChargeCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
        cell.backgroundColor = [UIColor clearColor];
    }
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    cell.priceText.delegate = self;
    cell.desText.delegate = self;
    
    cell.idLabel.text = [NSString stringWithFormat:@"规格%ld", indexPath.row + 1 ];
    
    cell.priceText.tag = 1000+indexPath.row;
    [cell.priceText addTarget:self action:@selector(rightFieldChanged:) forControlEvents:UIControlEventEditingChanged];
    cell.desText.tag = 2000+indexPath.row;
    [cell.desText addTarget:self action:@selector(leftFieldChanged:) forControlEvents:UIControlEventEditingChanged];
    
    
    NSString *leftStr = self.chargeData[indexPath.row][@"des"];
    NSNumber *rightNum = self.chargeData[indexPath.row][@"price"];
    
//    [self.chargeData[indexPath.row] setObject:[NSString stringWithFormat:@"%ld",(long)indexPath.row+1] forKey:@"id"];
    
    cell.desText.text = [NSString stringWithFormat:@"%@", leftStr];
    cell.priceText.text = [NSString stringWithFormat:@"%@", rightNum];
    
    
    
    
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    UITextField *textField = [self.view viewWithTag:self.textTag];
    if (textField) {
        [textField endEditing:YES];
    }
    
}


- (UITableViewCellEditingStyle)tableView:(UITableView *)tableView editingStyleForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return UITableViewCellEditingStyleDelete;
}

- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
{
    return YES;
}

- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
{
    
//    NSDictionary * dic = self.chargeData[indexPath.row];
    [self.chargeData removeObjectAtIndex:indexPath.row];
    [self.tableView reloadData];
    
}





- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    return ChargeCellHeight;
    
}





- (void)setRightBtn{
    
    UIButton* rightBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    rightBtn.frame = CGRectMake(APP_VIEW_WIDTH - 44, (44-APP_NAV_LEFT_ITEM_HEIGHT)/2 + (APP_STATUSBAR_HEIGHT), 44, APP_NAV_LEFT_ITEM_HEIGHT);
    rightBtn.backgroundColor = [UIColor clearColor];
    [rightBtn addTarget:self action:@selector(clickFinish:) forControlEvents:UIControlEventTouchUpInside];
    rightBtn.tag = 10001;
    rightBtn.titleEdgeInsets = UIEdgeInsetsMake(0, 0, 0, 5);
    [rightBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [rightBtn setTitle:@"完成" forState:UIControlStateNormal];
    [self.navigationView addSubview:rightBtn];
    
    
}

- (void)clickFinish:(UIButton *)sender {
    if (sender.tag == 10001) {
        NSLog(@"----->完成");
        
        
        for (NSDictionary *dic in self.chargeData) {
            
            NSString *des = [dic objectForKey:@"des"];
            if (des.length == 0) {
                CSAlert(@"请输入规格");
                return;
            }
            
            
            NSNumber *number = [dic objectForKey:@"price"];
            if (number.intValue < 0|| [number  isEqual: @""]) {
                CSAlert(@"请输入金额");
                return;
            }
            
        }
        
        
        
        NSMutableDictionary *dic = [[NSMutableDictionary alloc] init];
        [dic setObject:self.chargeData forKey:@"chargeData"];
        
        
        
        
        
        
        NSNotification *notification =[NSNotification notificationWithName:@"changeViewController"
                                                                    object:nil
                                                                  userInfo:dic];
        
        [[NSNotificationCenter defaultCenter]postNotification:notification];
        [self.navigationController popViewControllerAnimated:YES];
    }
}


#pragma mark - Keyboard notification

- (void)onKeyboardNotification:(NSNotification *)notification {
    //Reset constraint constant by keyboard height
    if ([notification.name isEqualToString:UIKeyboardWillShowNotification]) {
        CGRect keyboardFrame = ((NSValue *) notification.userInfo[UIKeyboardFrameEndUserInfoKey]).CGRectValue;
        
        _tableView.frame = CGRectMake(_tableView.frame.origin.x, _tableView.frame.origin.y, _tableView.frame.size.width, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y-keyboardFrame.size.height);
        
    } else if ([notification.name isEqualToString:UIKeyboardWillHideNotification]) {
        self.textTag = 0;
        _tableView.frame = CGRectMake(_tableView.frame.origin.x, _tableView.frame.origin.y, _tableView.frame.size.width, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y-50);
        
        
        
    }
    
    //Animate change
    [UIView animateWithDuration:0.8f animations:^{
        [self.view layoutIfNeeded];
    }];
}




@end
