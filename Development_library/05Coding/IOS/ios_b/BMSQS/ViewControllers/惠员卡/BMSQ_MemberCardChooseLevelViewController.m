//
//  BMSQ_MemberCardChooseLevelViewController.m
//  BMSQS
//
//  Created by lxm on 15/7/29.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_MemberCardChooseLevelViewController.h"
#import "BMSQ_MemberCardEditViewController.h"

@interface BMSQ_MemberCardChooseLevelViewController ()

@end

@implementation BMSQ_MemberCardChooseLevelViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self.navigationItem setTitle:@"会员卡规则"];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if([segue.identifier isEqualToString:@"BMSQ_MemberCardEdit"]){
        BMSQ_MemberCardEditViewController *aVC = (BMSQ_MemberCardEditViewController*)segue.destinationViewController;
        aVC.cardDataArray = self.cardDataArray;
        aVC.level = @"0";
    }
    
}


#pragma mark -Delegate

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return 1;
}
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 44.f;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *cellIdentifier = nil;
    cellIdentifier = @"MemberTableViewCell";

    UITableViewCell *setCell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier];
    if (setCell == nil) {
        setCell = [[UITableViewCell alloc] initWithStyle:0 reuseIdentifier:cellIdentifier];
        
    }else{

    }
    _levelTextField = (UITextField *)[setCell.contentView viewWithTag:101];
    
    setCell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    return setCell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    
}

- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section{
    UIView *v = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 80)];
    v.backgroundColor = [UIColor clearColor];
    UIButton *btn = [[UIButton alloc] initWithFrame:CGRectMake(15,27,tableView.frame.size.width-30,44)];
    btn.backgroundColor = [UIColor colorWithHexString:@"0xC5000A"];
    btn.layer.cornerRadius = 4.0f;
    btn.titleLabel.font = [UIFont systemFontOfSize:17.f];
    [btn setTitle:NSLocalizedString(@"下一步", @"下一步") forState:UIControlStateNormal];
    [btn setTitle:NSLocalizedString(@"下一步", @"下一步") forState:UIControlStateHighlighted];
    [btn addTarget:self action:@selector(setupBtnClicked) forControlEvents:UIControlEventTouchUpInside];
    btn.showsTouchWhenHighlighted = YES;
    [v addSubview:btn];
    return v;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section;
{
    return 100.f;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section;
{
    return 18.f;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UIView *v = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 9)];
    v.backgroundColor = [UIColor clearColor];
    return v;
}

- (void)setupBtnClicked
{
//    NSMutableDictionary *dicTemp = [[NSMutableDictionary alloc] init];
//    NSString *userStr = [_levelTextField.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
//    if(!userStr||userStr.length==0){
//        CSAlert(@"请输入等级");
//        return;
//    }else{
//        [dicTemp setObject:userStr forKey:@"mobileNbr"];
//    }

    [self performSegueWithIdentifier:@"BMSQ_MemberCardEdit" sender:nil];

}
@end
