//
//  BMSQ_educationTeacherListViewController.m
//  BMSQC
//
//  Created by liuqin on 16/3/11.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_educationTeacherListViewController.h"

#import "BMSQ_TeaDetailViewController.h"

#import "TeactherCollectionCell.h"
#import "MobClick.h"
@interface BMSQ_educationTeacherListViewController ()<UICollectionViewDataSource,UICollectionViewDelegate,UICollectionViewDelegateFlowLayout>


@property(nonatomic, strong)UICollectionView *collectionView;
@property(nonatomic, strong)NSMutableArray *teacherList;


@end


@implementation BMSQ_educationTeacherListViewController

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [MobClick beginLogPageView:@"educationTeacherList"];// 
}
- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [MobClick endLogPageView:@"educationTeacherList"];
}


-(void)viewDidLoad{
    [super viewDidLoad];
    
    [self setNavBackItem];
    [self setNavigationBar];
    [self setNavTitle:@"名师堂"];
    [self listShopTeacher];

    self.teacherList = [[NSMutableArray alloc]init];
    UICollectionViewFlowLayout *flowLayout = [[UICollectionViewFlowLayout alloc]init];
    [flowLayout setScrollDirection:UICollectionViewScrollDirectionVertical];
    self.collectionView = [[UICollectionView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y) collectionViewLayout:flowLayout];
    self.collectionView.backgroundColor = [UIColor clearColor];
    self.collectionView.delegate = self;
    self.collectionView.dataSource = self;
    [self.view addSubview:self.collectionView];
    
    [self.collectionView registerClass:[TeactherCollectionCell class] forCellWithReuseIdentifier:@"teacherCell"];

}

#pragma mark --UICollectionViewDataSource,UICollectionViewDelegate

-(NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section{
 
    
    return self.teacherList.count;
    
}
-(NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView{
    return 1;
}

-(UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath{
    
    
    static NSString *identifer = @"teacherCell";
    
    
    TeactherCollectionCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:identifer forIndexPath:indexPath];
    
    NSDictionary *dic = [self.teacherList objectAtIndex:indexPath.row];
    cell.teacherCode = [dic objectForKey:@"teacherCode"];
    [cell setTeacher:dic row:(int)indexPath.row];
    
    return cell;
    
    
    
}

-(CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath{
    return CGSizeMake(APP_VIEW_WIDTH/2, (APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)/2+20);
}

 -(UIEdgeInsets)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout insetForSectionAtIndex:(NSInteger)section{
    
    return UIEdgeInsetsMake(0,0,0, 0);
    
}
- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout minimumInteritemSpacingForSectionAtIndex:(NSInteger)section{
    return 0;
}
-(CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout minimumLineSpacingForSectionAtIndex:(NSInteger)section{
    return 0;
}



-(void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath{
    
    BMSQ_TeaDetailViewController *vc = [[BMSQ_TeaDetailViewController alloc]init];
    NSDictionary *dic = [self.teacherList objectAtIndex:indexPath.row];
    vc.teacherCode = [dic objectForKey:@"teacherCode"];
    vc.teaTitel = [dic objectForKey:@"teacherTitle"];
    vc.isTeacher = YES;
    [self.navigationController pushViewController:vc animated:YES];
    
    
    
}
-(BOOL)collectionView:(UICollectionView *)collectionView shouldSelectItemAtIndexPath:(NSIndexPath *)indexPath{
    return  YES;
}
-(void)listShopTeacher{
    [self initJsonPrcClient:@"2"];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.shopCode forKey:@"shopCode"];
    [params setObject:@"0" forKey:@"page"];  //
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];  //

    NSString* vcode = [gloabFunction getSign:@"listShopTeacher" strParams:self.shopCode];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"listShopTeacher" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        NSArray *array = [responseObject objectForKey:@"list"];
        [weakSelf.teacherList addObjectsFromArray:array];
        [weakSelf.collectionView reloadData];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"asdfsafd");
    }];

}
@end
